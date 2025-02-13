package com.entasis.trading.collector.exchange;

import com.entasis.trading.collector.FuturesConnector;
import com.entasis.trading.dto.FuturesMarketData;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.Collections;

@Slf4j
@Component
public class BinanceFuturesExchange implements FuturesConnector {
    private final WebClient webClient;

    public BinanceFuturesExchange(@Qualifier("futuresWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public CompletableFuture<List<FuturesMarketData>> fetchFuturesData(String exchange, String symbol) {
        return webClient.get()
            .uri("/fapi/v1/ticker/24hr?symbol=" + symbol.replace("/", ""))
            .retrieve()
            .bodyToMono(JsonNode.class)
            .doOnNext(response -> log.info("Futures API Response: {}", response))
            .doOnError(error -> log.error("Error fetching futures data: {}", error.getMessage()))
            .map(this::convertToFuturesData)
            .toFuture();
    }

    private List<FuturesMarketData> convertToFuturesData(JsonNode data) {
        if (data == null) {
            log.error("Received null data from futures API");
            return Collections.emptyList();
        }

        try {
            FuturesMarketData marketData = new FuturesMarketData();
            marketData.setSymbol(data.get("symbol").asText());
            marketData.setExchange("binance");
            marketData.setTimestamp(LocalDateTime.now());
            marketData.setPrice(new BigDecimal(data.get("lastPrice").asText()));
            marketData.setVolume(new BigDecimal(data.get("volume").asText()));
            marketData.setOpen(new BigDecimal(data.get("openPrice").asText()));
            marketData.setHigh(new BigDecimal(data.get("highPrice").asText()));
            marketData.setLow(new BigDecimal(data.get("lowPrice").asText()));
            marketData.setClose(new BigDecimal(data.get("lastPrice").asText()));
            
            return List.of(marketData);
        } catch (Exception e) {
            log.error("Error converting futures data: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
} 