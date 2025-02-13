package com.entasis.trading.collector;

import com.entasis.trading.dto.SpotMarketData;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ExchangeConnector {
    CompletableFuture<List<SpotMarketData>> fetchMarketData(String exchange, String symbol);
}