package com.entasis.trading.service;

import com.entasis.trading.dto.FuturesMarketData;
import java.util.List;

public interface FuturesMarketDataCollectorService {
    void startCollecting(List<String> symbols, List<String> exchanges);
    void stopCollecting();
    FuturesMarketData getLatestMarketData(String symbol, String exchange);
} 