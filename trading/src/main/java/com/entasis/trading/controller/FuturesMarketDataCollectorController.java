package com.entasis.trading.controller;

import com.entasis.trading.collector.FuturesMarketDataCollector;
import com.entasis.trading.controller.request.CollectorRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collector/futures")
public class FuturesMarketDataCollectorController {
    private final FuturesMarketDataCollector futuresCollector;

    @PostMapping("/start")
    public void startCollecting(@RequestBody CollectorRequest request) {
        futuresCollector.startCollecting(request.getSymbols(), request.getExchanges());
    }

    @PostMapping("/stop")
    public void stopCollecting() {
        futuresCollector.stopCollecting();
    }
} 