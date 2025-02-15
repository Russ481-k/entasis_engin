package com.entasis.trading.collector.impl;

import com.entasis.trading.collector.FuturesMarketDataCollector;
import com.entasis.trading.collector.exchange.BinanceFuturesExchange;
import com.entasis.trading.dto.FuturesMarketData;
import com.entasis.trading.service.FuturesMarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuturesMarketDataCollectorImpl implements FuturesMarketDataCollector {
    private final BinanceFuturesExchange futuresConnector;
    private final FuturesMarketDataService futuresMarketDataService;
    private final Map<String, Boolean> collectionStatus = new ConcurrentHashMap<>();
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

    @Scheduled(fixedRate = 2000)
    public void collectData() {
        List<String> symbols = Arrays.asList("BTCUSDT", "ETHUSDT");
        symbols.forEach(symbol -> {  // parallelStream() 제거하여 순차 처리로 변경
            try {
                FuturesMarketData marketData = futuresConnector.getMarketData(symbol);
                futuresMarketDataService.save(marketData);
            } catch (Exception e) {
                log.error("Error processing futures data for {}: {}", symbol, e.getMessage());
            }
        });
    }

    private String getKey(String symbol, String exchange) {
        return String.format("%s_%s", symbol, exchange);
    }
} 