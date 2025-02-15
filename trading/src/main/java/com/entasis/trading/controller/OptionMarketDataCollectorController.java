package com.entasis.trading.controller;

import com.entasis.trading.collector.OptionMarketDataCollector;
import com.entasis.trading.controller.request.CollectorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collector/options")
public class OptionMarketDataCollectorController {
    private final OptionMarketDataCollector optionsCollector;

    @PostMapping("/start")
    public void startCollecting(@RequestBody CollectorRequest request) {
        optionsCollector.startCollecting(request.getCurrencies(), request.getExchanges());
    }

    @PostMapping("/stop")
    public void stopCollecting() {
        optionsCollector.stopCollecting();
    }
} 