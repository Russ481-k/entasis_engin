package com.entasis.trading.config;

import com.entasis.trading.service.FuturesMarketDataCollectorService;
import com.entasis.trading.service.SpotMarketDataCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataCollectorConfig {
    
    private final SpotMarketDataCollectorService spotCollectorService;
    private final FuturesMarketDataCollectorService futuresCollectorService;
    
    @EventListener(ApplicationReadyEvent.class)
    public void startCollecting() {
        // 수집할 심볼과 거래소 설정
        List<String> symbols = List.of("BTCUSDT", "ETHUSDT");
        List<String> exchanges = List.of("binance");
        
        // 현물 데이터 수집 시작
        spotCollectorService.startCollecting(symbols, exchanges);
        
        // 선물 데이터 수집 시작
        futuresCollectorService.startCollecting(symbols, exchanges);
    }
} 