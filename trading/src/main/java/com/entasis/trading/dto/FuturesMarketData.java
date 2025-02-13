package com.entasis.trading.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FuturesMarketData {
    private String symbol;
    private String exchange;
    private LocalDateTime timestamp;
    private BigDecimal price;
    private BigDecimal volume;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal openInterest;
    private BigDecimal fundingRate;
    private BigDecimal markPrice;
    private BigDecimal indexPrice;
} 