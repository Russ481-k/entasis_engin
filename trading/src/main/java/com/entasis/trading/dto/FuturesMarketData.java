package com.entasis.trading.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class FuturesMarketData extends BaseMarketDataDto implements MarketData {
    private BigDecimal openInterest;
    private BigDecimal fundingRate;
    private BigDecimal markPrice;
    private BigDecimal indexPrice;
    
    // OHLC 필드 추가
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
} 