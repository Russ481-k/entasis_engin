package com.entasis.trading.service;

import com.entasis.trading.dto.FuturesMarketData;
import java.time.LocalDateTime;
import java.util.List;

public interface FuturesMarketDataService {
    FuturesMarketData save(FuturesMarketData marketData);
    void saveAll(List<FuturesMarketData> marketDataList);
    List<FuturesMarketData> findBySymbolAndExchange(String symbol, String exchange);
    List<FuturesMarketData> findBySymbol(String symbol);
    List<FuturesMarketData> findBySymbolAndTimeRange(String symbol, LocalDateTime startTime, LocalDateTime endTime);
    List<FuturesMarketData> findByExchange(String exchange);
    void deleteById(Long id);
} 