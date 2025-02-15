package com.entasis.trading.collector.impl;

import com.entasis.trading.collector.OptionMarketDataCollector;
import com.entasis.trading.collector.exchange.DeribitOptionExchange;
import com.entasis.trading.service.OptionMarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OptionMarketDataCollectorImpl implements OptionMarketDataCollector {
    
    private final DeribitOptionExchange deribitExchange;
    private final OptionMarketDataService optionMarketDataService;

    @Override
    public void startCollecting(List<String> currencies, List<String> exchanges) {
        log.info("Initializing options data collection...");
        
        currencies.forEach(currency -> {
            try {
                log.info("Fetching options instruments for currency: {}", currency);
                List<String> instruments = deribitExchange.getOptionsInstruments(currency);
                log.info("Found {} options instruments", instruments.size());
                
                instruments.forEach(instrument -> {
                    try {
                        log.info("Collecting market data for instrument: {}", instrument);
                        optionMarketDataService.collectOptionsMarketData(instrument);
                    } catch (Exception e) {
                        log.error("Failed to collect market data for instrument {}: {}", 
                            instrument, e.getMessage(), e);
                    }
                });
            } catch (Exception e) {
                log.error("Failed to fetch instruments for currency {}: {}", 
                    currency, e.getMessage(), e);
            }
        });
    }

    @Override
    public void stopCollecting() {
        // Implementation needed
    }

    @Override
    public void collectMarketData() {
        // Implementation needed
    }
} 