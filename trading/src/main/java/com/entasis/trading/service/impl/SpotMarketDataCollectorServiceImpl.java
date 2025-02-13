package com.entasis.trading.service.impl;

import com.entasis.trading.collector.ExchangeConnector;
import com.entasis.trading.dto.SpotMarketData;
import com.entasis.trading.service.SpotMarketDataCollectorService;
import com.entasis.trading.service.SpotMarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotMarketDataCollectorServiceImpl implements SpotMarketDataCollectorService {
    private final ExchangeConnector exchangeConnector;
    private final SpotMarketDataService spotMarketDataService;
    private final ConcurrentHashMap<String, Boolean> collectionStatus = new ConcurrentHashMap<>();
    private List<String> activeSymbols;
    private List<String> activeExchanges;

    @Override
    public void startCollecting(List<String> symbols, List<String> exchanges) {
        this.activeSymbols = symbols;
        this.activeExchanges = exchanges;
        symbols.forEach(symbol -> 
            exchanges.forEach(exchange -> 
                collectionStatus.put(getKey(symbol, exchange), true)
            )
        );
    }

    @Override
    public void stopCollecting() {
        collectionStatus.clear();
    }

    @Override
    public SpotMarketData getLatestMarketData(String symbol, String exchange) {
        return spotMarketDataService.findBySymbolAndExchange(symbol, exchange).get(0);
    }

    @Scheduled(fixedRate = 1000)
    public void collectMarketData() {
        if (collectionStatus.isEmpty()) return;

        activeSymbols.forEach(symbol -> 
            activeExchanges.forEach(exchange -> {
                if (Boolean.TRUE.equals(collectionStatus.get(getKey(symbol, exchange)))) {
                    try {
                        CompletableFuture<List<SpotMarketData>> future = exchangeConnector.fetchMarketData(exchange, symbol);
                        future.thenAccept(marketDataList -> {
                            spotMarketDataService.saveAll(marketDataList);
                        }).exceptionally(throwable -> {
                            log.error("Error processing market data for {}/{}: {}", 
                                exchange, symbol, throwable.getMessage());
                            return null;
                        });
                    } catch (Exception e) {
                        log.error("Error collecting market data for {}/{}: {}", 
                            exchange, symbol, e.getMessage());
                    }
                }
            })
        );
    }

    private String getKey(String symbol, String exchange) {
        return String.format("%s_%s", symbol, exchange);
    }
} 