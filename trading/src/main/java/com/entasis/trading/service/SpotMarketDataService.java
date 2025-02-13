package com.entasis.trading.service;

import com.entasis.trading.dto.SpotMarketData;
import java.time.LocalDateTime;
import java.util.List;

public interface SpotMarketDataService {
    SpotMarketData save(SpotMarketData marketData);
    void saveAll(List<SpotMarketData> marketDataList);
    List<SpotMarketData> findBySymbolAndExchange(String symbol, String exchange);
    List<SpotMarketData> findBySymbol(String symbol);
    List<SpotMarketData> findBySymbolAndTimeRange(String symbol, LocalDateTime startTime, LocalDateTime endTime);
    List<SpotMarketData> findByExchange(String exchange);
    void deleteById(Long id);
} 