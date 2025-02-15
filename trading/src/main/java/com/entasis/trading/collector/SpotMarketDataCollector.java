package com.entasis.trading.collector;

import java.util.List;

public interface SpotMarketDataCollector {
    void startCollecting(List<String> symbols, List<String> exchanges);
    void stopCollecting();
} 