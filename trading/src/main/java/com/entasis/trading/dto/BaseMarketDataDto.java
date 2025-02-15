package com.entasis.trading.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseMarketDataDto implements MarketData {
    private Long id;
    private String symbol;
    private String exchange;
    private LocalDateTime timestamp;
    private BigDecimal price;
    private BigDecimal volume;
} 