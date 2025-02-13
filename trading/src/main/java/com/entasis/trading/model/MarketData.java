package com.entasis.trading.model;

import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MarketData {
    private Long id;
    private String symbol;
    private String exchange;
    private LocalDateTime timestamp;
    private BigDecimal price;
    private BigDecimal volume;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private LocalDateTime createdAt;
} 