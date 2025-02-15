package com.entasis.trading.service;

import com.entasis.trading.dto.OptionsMarketData;
import java.time.LocalDateTime;
import java.util.List;

public interface OptionsMarketDataService {
    void syncOptionsInstruments(String currency);
    void collectOptionsMarketData(String instrumentName);
    OptionsMarketData save(OptionsMarketData marketData);
    List<OptionsMarketData> findBySymbol(String symbol);
    List<OptionsMarketData> findBySymbolAndTimeRange(String symbol, LocalDateTime startTime, LocalDateTime endTime);
    List<OptionsMarketData> getMarketDataBySymbol(String symbol, LocalDateTime startTime, LocalDateTime endTime);
} 
