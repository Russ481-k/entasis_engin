package com.entasis.trading.controller;

import com.entasis.trading.dto.SpotMarketData;
import com.entasis.trading.service.SpotMarketDataCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/spot")
@RequiredArgsConstructor
public class SpotMarketDataCollectorController {
    
    private final SpotMarketDataCollectorService spotMarketDataCollectorService;

    @PostMapping("/collector/start")
    public void startCollecting(@RequestBody Map<String, List<String>> request) {
        spotMarketDataCollectorService.startCollecting(
            request.get("symbols"),
            request.get("exchanges")
        );
    }

    @PostMapping("/collector/stop")
    public void stopCollecting() {
        spotMarketDataCollectorService.stopCollecting();
    }

    @GetMapping("/data/{exchange}/{symbol}")
    public SpotMarketData getLatestMarketData(
        @PathVariable String exchange,
        @PathVariable String symbol
    ) {
        return spotMarketDataCollectorService.getLatestMarketData(symbol, exchange);
    }
} 