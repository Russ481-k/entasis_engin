package com.entasis.trading.service;

import com.entasis.trading.dto.MarketData;
import java.time.LocalDateTime;
import java.util.List;

public interface MarketDataService {
    MarketData save(MarketData marketData);
    List<MarketData> findBySymbol(String symbol);
    List<MarketData> findBySymbolAndTimeRange(String symbol, LocalDateTime startTime, LocalDateTime endTime);
    List<MarketData> findByExchange(String exchange);
    void deleteById(Long id);
} 