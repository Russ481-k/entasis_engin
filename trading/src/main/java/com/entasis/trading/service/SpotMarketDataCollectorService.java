package com.entasis.trading.service;

import com.entasis.trading.dto.SpotMarketData;
import java.util.List;

public interface SpotMarketDataCollectorService {
    void startCollecting(List<String> symbols, List<String> exchanges);
    void stopCollecting();
    SpotMarketData getLatestMarketData(String symbol, String exchange);
} 