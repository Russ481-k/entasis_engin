package com.entasis.trading.collector.exchange;

import com.entasis.trading.collector.ExchangeConnector;
import com.entasis.trading.dto.SpotMarketData;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class BinanceExchange implements ExchangeConnector {
    private final WebClient webClient;

    public BinanceExchange(@Qualifier("spotWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public CompletableFuture<List<SpotMarketData>> fetchMarketData(String exchange, String symbol) {
        return webClient.get()
            .uri("/api/v3/ticker/24hr?symbol=" + symbol)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .map(this::convertToMarketData)
            .doOnError(error -> log.error("Error fetching market data: {}", error.getMessage()))
            .toFuture();
    }

    private List<SpotMarketData> convertToMarketData(JsonNode data) {
        SpotMarketData marketData = new SpotMarketData();
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
    }

    public SpotMarketData getMarketData(String symbol) {
        String url = String.format("/api/v3/ticker/24hr?symbol=%s", symbol);
        JsonNode response = webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .block();
            
        SpotMarketData marketData = new SpotMarketData();
        marketData.setSymbol(symbol);
        marketData.setPrice(new BigDecimal(response.get("lastPrice").asText()));
        marketData.setVolume(new BigDecimal(response.get("volume").asText()));
        marketData.setTimestamp(LocalDateTime.now());
        
        return marketData;
    }
} 