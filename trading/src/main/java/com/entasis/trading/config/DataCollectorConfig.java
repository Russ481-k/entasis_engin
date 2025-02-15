package com.entasis.trading.config;

import com.entasis.trading.collector.FuturesMarketDataCollector;
import com.entasis.trading.collector.SpotMarketDataCollector;
import com.entasis.trading.collector.OptionMarketDataCollector;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataCollectorConfig {
    
    private final SpotMarketDataCollector spotCollector;
    private final FuturesMarketDataCollector futuresCollector;
    private final OptionMarketDataCollector optionsCollector;
    
    private static final Logger log = LoggerFactory.getLogger(DataCollectorConfig.class);
    
    @EventListener(ApplicationReadyEvent.class)
    public void startDataCollection() {
        List<String> currencies = List.of("BTC");
        List<String> optionsExchanges = List.of("deribit");
        
        log.info("Starting options data collection for currencies: {}, exchanges: {}", 
            currencies, optionsExchanges);
        optionsCollector.startCollecting(currencies, optionsExchanges);
        
        // 선물/현물 데이터 수집 비활성화
        // spotCollector.startCollecting(symbols, exchanges);
        // futuresCollector.startCollecting(symbols, exchanges);
    }
} 