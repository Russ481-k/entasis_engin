package com.entasis.trading.service;

import com.entasis.trading.entity.enums.AggregationPeriod;
import com.entasis.trading.entity.enums.InstrumentType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VolumeAggregationService {
    void aggregateVolume(String symbol, InstrumentType instrumentType, AggregationPeriod periodType);
    BigDecimal getVolume(String symbol, InstrumentType instrumentType, LocalDateTime startTime, LocalDateTime endTime);
    List<BigDecimal> getVolumeHistory(String symbol, InstrumentType instrumentType, AggregationPeriod periodType, LocalDateTime startTime, LocalDateTime endTime);
} 