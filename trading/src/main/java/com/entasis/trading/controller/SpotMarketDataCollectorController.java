package com.entasis.trading.controller;

import com.entasis.trading.collector.SpotMarketDataCollector;
import com.entasis.trading.controller.request.CollectorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collector/spot")
public class SpotMarketDataCollectorController {
    private final SpotMarketDataCollector spotCollector;

    @PostMapping("/start")
    public void startCollecting(@RequestBody CollectorRequest request) {
        spotCollector.startCollecting(request.getSymbols(), request.getExchanges());
    }

    @PostMapping("/stop")
    public void stopCollecting() {
        spotCollector.stopCollecting();
    }
} 