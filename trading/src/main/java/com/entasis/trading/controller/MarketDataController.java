package com.entasis.trading.controller;

import com.entasis.trading.dto.MarketData;
import com.entasis.trading.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/market-data")
@RequiredArgsConstructor
public class MarketDataController {

    private final MarketDataService marketDataService;

    @PostMapping
    public ResponseEntity<MarketData> createMarketData(@RequestBody MarketData marketData) {
        return ResponseEntity.ok(marketDataService.save(marketData));
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<List<MarketData>> getBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(marketDataService.findBySymbol(symbol));
    }

    @GetMapping("/symbol/{symbol}/range")
    public ResponseEntity<List<MarketData>> getBySymbolAndTimeRange(
            @PathVariable String symbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(marketDataService.findBySymbolAndTimeRange(symbol, startTime, endTime));
    }

    @GetMapping("/exchange/{exchange}")
    public ResponseEntity<List<MarketData>> getByExchange(@PathVariable String exchange) {
        return ResponseEntity.ok(marketDataService.findByExchange(exchange));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarketData(@PathVariable Long id) {
        marketDataService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 