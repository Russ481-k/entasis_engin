package com.entasis.trading.controller;

import com.entasis.trading.dto.FuturesMarketData;
import com.entasis.trading.service.FuturesMarketDataCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/futures")
@RequiredArgsConstructor
public class FuturesMarketDataCollectorController {
    
    private final FuturesMarketDataCollectorService futuresMarketDataCollectorService;

    @PostMapping("/collector/start")
    public void startCollecting(@RequestBody Map<String, List<String>> request) {
        futuresMarketDataCollectorService.startCollecting(
            request.get("symbols"),
            request.get("exchanges")
        );
    }

    @PostMapping("/collector/stop")
    public void stopCollecting() {
        futuresMarketDataCollectorService.stopCollecting();
    }

    @GetMapping("/data/{exchange}/{symbol}")
    public FuturesMarketData getLatestMarketData(
        @PathVariable String exchange,
        @PathVariable String symbol
    ) {
        return futuresMarketDataCollectorService.getLatestMarketData(symbol, exchange);
    }
} 