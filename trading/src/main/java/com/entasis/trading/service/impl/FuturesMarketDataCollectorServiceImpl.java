package com.entasis.trading.service.impl;

import com.entasis.trading.collector.FuturesConnector;
import com.entasis.trading.dto.FuturesMarketData;
import com.entasis.trading.service.FuturesMarketDataCollectorService;
import com.entasis.trading.service.FuturesMarketDataService;
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
public class FuturesMarketDataCollectorServiceImpl implements FuturesMarketDataCollectorService {
    private final FuturesConnector futuresConnector;
    private final FuturesMarketDataService futuresMarketDataService;
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
    public FuturesMarketData getLatestMarketData(String symbol, String exchange) {
        return futuresMarketDataService.findBySymbolAndExchange(symbol, exchange).get(0);
    }

    @Scheduled(fixedRate = 1000)
    public void collectMarketData() {
        if (collectionStatus.isEmpty()) return;

        activeSymbols.forEach(symbol -> 
            activeExchanges.forEach(exchange -> {
                if (Boolean.TRUE.equals(collectionStatus.get(getKey(symbol, exchange)))) {
                    try {
                        CompletableFuture<List<FuturesMarketData>> future = futuresConnector.fetchFuturesData(exchange, symbol);
                        future.thenAccept(marketDataList -> {
                            futuresMarketDataService.saveAll(marketDataList);
                            marketDataList.forEach(data -> 
                                log.info("Collected futures market data for {}/{}: price={}, volume={}, funding_rate={}", 
                                    exchange, symbol, data.getPrice(), data.getVolume(), data.getFundingRate())
                            );
                        }).exceptionally(throwable -> {
                            log.error("Error processing futures data for {}/{}: {}", 
                                exchange, symbol, throwable.getMessage());
                            return null;
                        });
                    } catch (Exception e) {
                        log.error("Error collecting futures data for {}/{}: {}", 
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