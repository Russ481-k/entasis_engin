package com.entasis.trading.collector.exchange;

import com.entasis.trading.dto.OptionMarketData;
import com.entasis.trading.exception.DeribitApiException;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import org.springframework.web.reactive.function.client.WebClient;
import com.entasis.trading.dto.OptionInstrument;
import com.entasis.trading.dto.Greeks;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.springframework.http.MediaType;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeribitOptionExchange {
    private final WebClient optionsWebClient;
    private final ObjectMapper objectMapper;

    public List<OptionInstrument> getInstruments(String currency) {
        String url = String.format("/public/get_instruments?currency=%s&kind=option&expired=false", currency);
        try {
            JsonNode response = optionsWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

            if (!response.has("result")) {
                log.error("Invalid response format from Deribit API: {}", response);
                throw new DeribitApiException("Invalid response format");
            }

            List<OptionInstrument> instruments = new ArrayList<>();
            response.get("result").forEach(instrument -> {
                OptionInstrument optionsInstrument = new OptionInstrument();
                optionsInstrument.setInstrumentName(instrument.get("instrument_name").asText());
                optionsInstrument.setSettlementPeriod(instrument.get("settlement_period").asText());
                optionsInstrument.setExpirationTimestamp(instrument.get("expiration_timestamp").asLong());
                optionsInstrument.setStrike(BigDecimal.valueOf(instrument.get("strike").asDouble()));
                optionsInstrument.setOptionType(instrument.get("option_type").asText());
                instruments.add(optionsInstrument);
            });
            
            return instruments;
        } catch (Exception e) {
            log.error("Failed to get instruments for {}: {}", currency, e.getMessage());
            throw new DeribitApiException("Failed to get instruments", e);
        }
    }

    public double getCurrentPrice(String instrumentName) {
        String url = String.format("/public/get_book_summary_by_instrument?instrument_name=%s", instrumentName);
        try {
            JsonNode response = optionsWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnNext(res -> log.info("Current price response for {}: {}", instrumentName, res.toPrettyString()))
                .block();

            if (!response.has("result") || response.get("result").isEmpty()) {
                return 0.0;  // 데이터가 없는 경우 0 반환
            }

            JsonNode result = response.get("result").get(0);
            return getDecimalValue(result, "mark_price", BigDecimal.ZERO).doubleValue();
        } catch (Exception e) {
            log.error("Failed to get current price for {}: {}", instrumentName, e.getMessage());
            return 0.0;  // 에러 발생시 0 반환
        }
    }

    public List<String> getRelevantStrikes(String currency, double currentPrice) {
        String url = String.format("/public/get_instruments?currency=%s&kind=option", currency);
        try {
            JsonNode response = optionsWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

            List<String> strikes = new ArrayList<>();
            response.get("result").forEach(instrument -> {
                String strike = instrument.get("strike").asText();
                if (Double.parseDouble(strike) > currentPrice) {
                    strikes.add(strike);
                }
            });
            return strikes;
        } catch (Exception e) {
            log.error("Failed to get strikes for {}: {}", currency, e.getMessage());
            throw new DeribitApiException("Failed to get strikes", e);
        }
    }

    public OptionMarketData getOptionsMarketData(String instrumentName) {
        try {
            JsonNode response = optionsWebClient.get()
                .uri("/api/v2/public/get_book_summary_by_instrument?instrument_name={name}", instrumentName)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnNext(res -> {
                    String jsonStr = res.toPrettyString();
                    if (jsonStr.length() > 1000) {
                        log.info("Raw response (truncated): {}...", jsonStr.substring(0, 1000));
                    } else {
                        log.info("Raw response: {}", jsonStr);
                    }
                })
                .block();

            if (!response.has("result") || response.get("result").isEmpty()) {
                log.error("Invalid or empty response for instrument {}: {}", instrumentName, response);
                throw new DeribitApiException("Invalid response format");
            }

            JsonNode result = response.get("result").get(0);
            log.info("Market data for {}: {}", instrumentName, result.toPrettyString());

            OptionMarketData marketData = new OptionMarketData();
            marketData.setInstrumentName(instrumentName);
            marketData.setPrice(getDecimalValue(result, "mark_price", BigDecimal.ZERO));
            marketData.setVolume(getDecimalValue(result, "volume", BigDecimal.ZERO));
            marketData.setUnderlyingPrice(getDecimalValue(result, "underlying_price", BigDecimal.ZERO));
            marketData.setOpenInterest(getDecimalValue(result, "open_interest", BigDecimal.ZERO));
            marketData.setBidPrice(getDecimalValue(result, "bid_price", BigDecimal.ZERO));
            marketData.setAskPrice(getDecimalValue(result, "ask_price", BigDecimal.ZERO));
            marketData.setImpliedVolatility(getDecimalValue(result, "mark_iv", BigDecimal.ZERO));
            marketData.setGreeks(getGreeks(result));
            marketData.setTimestamp(LocalDateTime.now());
            
            return marketData;
        } catch (Exception e) {
            log.error("Failed to get options market data for {}: {}", instrumentName, e.getMessage(), e);
            throw new DeribitApiException("Failed to get options market data", e);
        }
    }

    private Greeks getGreeks(JsonNode data) {
        Greeks greeks = new Greeks();
        greeks.setDelta(getDecimalValue(data, "mark_delta", BigDecimal.ZERO));
        greeks.setGamma(getDecimalValue(data, "mark_gamma", BigDecimal.ZERO));
        greeks.setVega(getDecimalValue(data, "mark_vega", BigDecimal.ZERO));
        greeks.setTheta(getDecimalValue(data, "mark_theta", BigDecimal.ZERO));
        greeks.setRho(getDecimalValue(data, "mark_rho", BigDecimal.ZERO));
        return greeks;
    }

    private BigDecimal getDecimalValue(JsonNode node, String fieldName, BigDecimal defaultValue) {
        if (node == null || !node.has(fieldName) || node.get(fieldName) == null || node.get(fieldName).isNull()) {
            return defaultValue;
        }
        try {
            return BigDecimal.valueOf(node.get(fieldName).asDouble());
        } catch (Exception e) {
            log.warn("Failed to parse {} value: {}", fieldName, node.get(fieldName));
            return defaultValue;
        }
    }

    public List<String> getOptionsInstruments(String currency) {
        try {
            String response = optionsWebClient.get()
                .uri("/api/v2/public/get_instruments?currency={currency}&kind=option&expired=false", currency)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(res -> {
                    if (res.length() > 1000) {
                        log.info("Raw response (truncated): {}...", res.substring(0, 1000));
                    } else {
                        log.info("Raw response: {}", res);
                    }
                })
                .block();
                
            JsonNode root = objectMapper.readTree(response);
            if (!root.has("result")) {
                log.error("Invalid response format: {}", response);
                return Collections.emptyList();
            }
            
            JsonNode result = root.get("result");
            List<String> instruments = new ArrayList<>();
            result.forEach(instrument -> {
                if (instrument.has("instrument_name")) {
                    instruments.add(instrument.get("instrument_name").asText());
                }
            });
            
            log.info("Successfully parsed {} instruments", instruments.size());
            if (!instruments.isEmpty()) {
                log.info("Sample instruments: {}", 
                    instruments.subList(0, Math.min(5, instruments.size())));
            }
            
            return instruments;
            
        } catch (Exception e) {
            log.error("Error fetching options instruments: {} - {}", 
                e.getClass().getSimpleName(), e.getMessage(), e);
            return Collections.emptyList();
        }
    }
} 