package com.entasis.trading.collector;

import java.util.List;

public interface OptionsMarketDataCollector {
    void startCollecting(List<String> currencies, List<String> exchanges);
    void stopCollecting();
} 