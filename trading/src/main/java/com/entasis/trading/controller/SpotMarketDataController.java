package com.entasis.trading.controller;

import com.entasis.trading.dto.SpotMarketData;
import com.entasis.trading.service.SpotMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/spot-market-data")
@RequiredArgsConstructor
public class SpotMarketDataController {

    private final SpotMarketDataService spotMarketDataService;

    @PostMapping
    public ResponseEntity<SpotMarketData> createMarketData(@RequestBody SpotMarketData marketData) {
        return ResponseEntity.ok(spotMarketDataService.save(marketData));
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<List<SpotMarketData>> getBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(spotMarketDataService.findBySymbol(symbol));
    }

    @GetMapping("/symbol/{symbol}/range")
    public ResponseEntity<List<SpotMarketData>> getBySymbolAndTimeRange(
            @PathVariable String symbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(spotMarketDataService.findBySymbolAndTimeRange(symbol, startTime, endTime));
    }

    @GetMapping("/exchange/{exchange}")
    public ResponseEntity<List<SpotMarketData>> getByExchange(@PathVariable String exchange) {
        return ResponseEntity.ok(spotMarketDataService.findByExchange(exchange));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarketData(@PathVariable Long id) {
        spotMarketDataService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 