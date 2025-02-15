package com.entasis.trading.service;

import com.entasis.trading.entity.OptionMarketDataEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface OptionMarketDataService {
    void syncOptionsInstruments(String currency);
    void collectOptionsMarketData(String instrumentName);
    
    List<OptionMarketDataEntity> getMarketDataByTimeRange(
        String underlyingAsset,
        LocalDateTime expiryDate,
        LocalDateTime startTime,
        LocalDateTime endTime
    );
} 
