package com.entasis.trading.collector.exchange;

import com.entasis.trading.dto.OptionsMarketData;
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
import com.entasis.trading.dto.OptionsInstrument;
import com.entasis.trading.dto.Greeks;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeribitOptionsExchange {
    private final WebClient optionsWebClient;

    public List<OptionsInstrument> getInstruments(String currency) {
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

            List<OptionsInstrument> instruments = new ArrayList<>();
            response.get("result").forEach(instrument -> {
                OptionsInstrument optionsInstrument = new OptionsInstrument();
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
        String url = "/api/v2/public/get_book_summary_by_instrument?instrument_name=" + instrumentName;
        try {
            JsonNode response = optionsWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

            JsonNode result = response.get("result").get(0);
            
            return result.get("mark_price").asDouble();
        } catch (Exception e) {
            log.error("Failed to get current price for {}: {}", instrumentName, e.getMessage());
            throw new DeribitApiException("Failed to get current price", e);
        }
    }

    public List<String> getRelevantStrikes(String currency, double currentPrice) {
        String url = "/api/v2/public/get_instruments?currency=" + currency + "&kind=option";
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

    public OptionsMarketData getOptionsMarketData(String instrumentName) {
        String url = "/api/v2/public/get_book_summary_by_instrument?instrument_name=" + instrumentName;
        try {
            JsonNode response = optionsWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

            JsonNode result = response.get("result").get(0);
            
            OptionsMarketData marketData = new OptionsMarketData();
            marketData.setSymbol(instrumentName);
            marketData.setPrice(BigDecimal.valueOf(result.get("mark_price").asDouble()));
            marketData.setVolume(BigDecimal.valueOf(result.get("volume").asDouble()));
            marketData.setUnderlyingPrice(BigDecimal.valueOf(result.get("underlying_price").asDouble()));
            marketData.setOpenInterest(BigDecimal.valueOf(result.get("open_interest").asDouble()));
            marketData.setBidPrice(BigDecimal.valueOf(result.get("bid_price").asDouble()));
            marketData.setAskPrice(BigDecimal.valueOf(result.get("ask_price").asDouble()));
            marketData.setImpliedVolatility(BigDecimal.valueOf(result.get("mark_iv").asDouble()));
            marketData.setGreeks(getGreeks(result));
            marketData.setTimestamp(LocalDateTime.now());
            
            return marketData;
        } catch (Exception e) {
            log.error("Failed to get options market data for {}: {}", instrumentName, e.getMessage());
            throw new DeribitApiException("Failed to get options market data", e);
        }
    }

    private Greeks getGreeks(JsonNode data) {
        Greeks greeks = new Greeks();
        greeks.setDelta(BigDecimal.valueOf(data.get("mark_delta").asDouble()));
        greeks.setGamma(BigDecimal.valueOf(data.get("mark_gamma").asDouble()));
        greeks.setVega(BigDecimal.valueOf(data.get("mark_vega").asDouble()));
        greeks.setTheta(BigDecimal.valueOf(data.get("mark_theta").asDouble()));
        greeks.setRho(BigDecimal.valueOf(data.get("mark_rho").asDouble()));
        return greeks;
    }
} 