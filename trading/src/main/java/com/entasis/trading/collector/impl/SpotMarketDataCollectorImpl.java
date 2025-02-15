package com.entasis.trading.collector.impl;

import com.entasis.trading.collector.SpotMarketDataCollector;
import com.entasis.trading.collector.exchange.BinanceExchange;
import com.entasis.trading.dto.SpotMarketData;
import com.entasis.trading.service.SpotMarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotMarketDataCollectorImpl implements SpotMarketDataCollector {
    private final BinanceExchange exchangeConnector;
    private final SpotMarketDataService spotMarketDataService;
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
        symbols.forEach(symbol -> {  // 순차 처리
            try {
                SpotMarketData marketData = exchangeConnector.getMarketData(symbol);
                spotMarketDataService.save(marketData);
            } catch (Exception e) {
                log.error("Error processing market data for {}: {}", symbol, e.getMessage());
            }
        });
    }

    private String getKey(String symbol, String exchange) {
        return String.format("%s_%s", symbol, exchange);
    }
} 