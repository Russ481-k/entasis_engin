package com.entasis.trading.config;

import com.entasis.trading.collector.FuturesMarketDataCollector;
import com.entasis.trading.collector.SpotMarketDataCollector;
import com.entasis.trading.collector.OptionsMarketDataCollector;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataCollectorConfig {
    
    private final SpotMarketDataCollector spotCollector;
    private final FuturesMarketDataCollector futuresCollector;
    private final OptionsMarketDataCollector optionsCollector;
    
    @EventListener(ApplicationReadyEvent.class)
    public void startCollecting() {
        // 수집할 심볼과 거래소 설정
        List<String> symbols = List.of("BTCUSDT", "ETHUSDT");
        List<String> currencies = List.of("BTC", "ETH");
        List<String> exchanges = List.of("binance");
        List<String> optionsExchanges = List.of("deribit");
        
        // 현물 데이터 수집 시작
        spotCollector.startCollecting(symbols, exchanges);
        
        // 선물 데이터 수집 시작
        futuresCollector.startCollecting(symbols, exchanges);
        
        // 옵션 데이터 수집 시작
        optionsCollector.startCollecting(currencies, optionsExchanges);
    }
} 