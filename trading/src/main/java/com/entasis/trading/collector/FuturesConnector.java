package com.entasis.trading.collector;

import com.entasis.trading.dto.FuturesMarketData;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FuturesConnector {
    CompletableFuture<List<FuturesMarketData>> fetchFuturesData(String exchange, String symbol);
    FuturesMarketData getMarketData(String symbol);
} 