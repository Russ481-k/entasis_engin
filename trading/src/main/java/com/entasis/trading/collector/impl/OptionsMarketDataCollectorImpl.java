package com.entasis.trading.collector.impl;

import com.entasis.trading.collector.OptionsMarketDataCollector;
import com.entasis.trading.collector.exchange.DeribitOptionsExchange;
import com.entasis.trading.service.OptionsMarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionsMarketDataCollectorImpl implements OptionsMarketDataCollector {
    private final DeribitOptionsExchange deribitExchange;
    private final OptionsMarketDataService optionsMarketDataService;
    private final Map<String, Boolean> collectionStatus = new ConcurrentHashMap<>();
    private List<String> activeCurrencies;
    private List<String> activeExchanges;

    @Override
    public void startCollecting(List<String> currencies, List<String> exchanges) {
        this.activeCurrencies = currencies;
        this.activeExchanges = exchanges;
        currencies.forEach(currency -> 
            exchanges.forEach(exchange -> 
                collectionStatus.put(getKey(currency, exchange), true)
            )
        );
    }

    @Override
    public void stopCollecting() {
        collectionStatus.clear();
    }

    @Scheduled(fixedRate = 1000)
    public void collectMarketData() {
        if (collectionStatus.isEmpty()) return;

        activeCurrencies.forEach(currency -> 
            activeExchanges.forEach(exchange -> {
                if (Boolean.TRUE.equals(collectionStatus.get(getKey(currency, exchange)))) {
                    try {
                        optionsMarketDataService.syncOptionsInstruments(currency);
                        optionsMarketDataService.findBySymbol(currency)
                            .forEach(optionsData -> {
                                try {
                                    optionsMarketDataService.collectOptionsMarketData(optionsData.getSymbol());
                                } catch (Exception e) {
                                    log.error("Error collecting options data for {}: {}", 
                                        optionsData.getSymbol(), e.getMessage());
                                }
                            });
                    } catch (Exception e) {
                        log.error("Error processing options data for {}/{}: {}", 
                            exchange, currency, e.getMessage());
                    }
                }
            })
        );
    }

    private String getKey(String currency, String exchange) {
        return String.format("%s_%s", currency, exchange);
    }
} 