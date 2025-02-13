package com.entasis.trading.controller;

import com.entasis.trading.dto.FuturesMarketData;
import com.entasis.trading.service.FuturesMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/futures-market-data")
@RequiredArgsConstructor
public class FuturesMarketDataController {

    private final FuturesMarketDataService futuresMarketDataService;

    @PostMapping
    public ResponseEntity<FuturesMarketData> createMarketData(@RequestBody FuturesMarketData marketData) {
        return ResponseEntity.ok(futuresMarketDataService.save(marketData));
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<List<FuturesMarketData>> getBySymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(futuresMarketDataService.findBySymbol(symbol));
    }

    @GetMapping("/symbol/{symbol}/range")
    public ResponseEntity<List<FuturesMarketData>> getBySymbolAndTimeRange(
            @PathVariable String symbol,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(futuresMarketDataService.findBySymbolAndTimeRange(symbol, startTime, endTime));
    }

    @GetMapping("/exchange/{exchange}")
    public ResponseEntity<List<FuturesMarketData>> getByExchange(@PathVariable String exchange) {
        return ResponseEntity.ok(futuresMarketDataService.findByExchange(exchange));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarketData(@PathVariable Long id) {
        futuresMarketDataService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 