package com.entasis.trading.service.impl;

import com.entasis.trading.entity.VolumeAggregation;
import com.entasis.trading.entity.Symbol;
import com.entasis.trading.entity.enums.AggregationPeriod;
import com.entasis.trading.entity.enums.InstrumentType;
import com.entasis.trading.entity.enums.ExchangeType;
import com.entasis.trading.repository.VolumeAggregationRepository;
import com.entasis.trading.repository.SymbolRepository;
import com.entasis.trading.repository.SpotMarketDataRepository;
import com.entasis.trading.repository.FuturesMarketDataRepository;
import com.entasis.trading.repository.OptionMarketDataRepository;
import com.entasis.trading.service.VolumeAggregationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.entasis.trading.entity.SpotMarketDataEntity;
import com.entasis.trading.entity.FuturesMarketDataEntity;
import com.entasis.trading.entity.OptionMarketDataEntity;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VolumeAggregationServiceImpl implements VolumeAggregationService {

    private final VolumeAggregationRepository volumeAggregationRepository;
    private final SymbolRepository symbolRepository;
    private final SpotMarketDataRepository spotMarketDataRepository;
    private final FuturesMarketDataRepository futuresMarketDataRepository;
    private final OptionMarketDataRepository optionMarketDataRepository;

    @Override
    @Transactional
    public void aggregateVolume(String symbol, InstrumentType instrumentType, AggregationPeriod periodType) {
        Symbol symbolObj = symbolRepository.findByExchangeSymbolAndInstrumentTypeAndExchange_Type(
            symbol, instrumentType, ExchangeType.valueOf(instrumentType.name()))
            .orElseThrow(() -> new RuntimeException("Symbol not found: " + symbol));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = calculateStartTime(now, periodType);
        
        VolumeAggregation aggregation = new VolumeAggregation();
        aggregation.setSymbol(symbolObj);
        aggregation.setInstrumentType(instrumentType);
        aggregation.setPeriodType(periodType);
        aggregation.setStartTime(startTime);
        aggregation.setEndTime(now);
        aggregation.setVolume(calculateVolume(symbol, instrumentType, startTime, now));
        
        volumeAggregationRepository.save(aggregation);
    }

    @Override
    @Cacheable(value = "volumes", key = "#symbol + #instrumentType + #startTime + #endTime")
    public BigDecimal getVolume(String symbol, InstrumentType instrumentType, LocalDateTime startTime, LocalDateTime endTime) {
        return volumeAggregationRepository.sumVolume(symbol, instrumentType, startTime, endTime);
    }

    @Override
    @Cacheable(value = "volumeHistories", key = "#symbol + #instrumentType + #periodType + #startTime + #endTime")
    public List<BigDecimal> getVolumeHistory(String symbol, InstrumentType instrumentType, AggregationPeriod periodType,
                                           LocalDateTime startTime, LocalDateTime endTime) {
        return volumeAggregationRepository.findVolumeAggregations(symbol, instrumentType, periodType, startTime, endTime)
            .stream()
            .map(VolumeAggregation::getVolume)
            .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 * * * * *")  // 매분 실행
    @Transactional
    public void aggregateMinuteVolumes() {
        symbolRepository.findAll().forEach(symbol -> {
            try {
                aggregateVolume(symbol.getExchangeSymbol(), symbol.getInstrumentType(), AggregationPeriod.MINUTE_1);
            } catch (Exception e) {
                log.error("Error aggregating volume for symbol: " + symbol.getExchangeSymbol(), e);
            }
        });
    }

    private LocalDateTime calculateStartTime(LocalDateTime endTime, AggregationPeriod periodType) {
        return switch (periodType) {
            case MINUTE_1 -> endTime.minus(1, ChronoUnit.MINUTES);
            case MINUTE_5 -> endTime.minus(5, ChronoUnit.MINUTES);
            case MINUTE_15 -> endTime.minus(15, ChronoUnit.MINUTES);
            case MINUTE_30 -> endTime.minus(30, ChronoUnit.MINUTES);
            case HOUR_1 -> endTime.minus(1, ChronoUnit.HOURS);
            case HOUR_4 -> endTime.minus(4, ChronoUnit.HOURS);
            case DAY_1 -> endTime.minus(1, ChronoUnit.DAYS);
            case WEEK_1 -> endTime.minus(7, ChronoUnit.DAYS);
        };
    }

    private BigDecimal calculateVolume(String symbol, InstrumentType instrumentType, LocalDateTime startTime, LocalDateTime endTime) {
        return switch (instrumentType) {
            case SPOT -> calculateSpotVolume(symbol, startTime, endTime);
            case FUTURES -> calculateFuturesVolume(symbol, startTime, endTime);
            case OPTION -> calculateOptionsVolume(symbol, startTime, endTime);
        };
    }

    private BigDecimal calculateSpotVolume(String symbol, LocalDateTime startTime, LocalDateTime endTime) {
        return spotMarketDataRepository.findBySymbol_ExchangeSymbolAndTimestampBetweenOrderByTimestampDesc(
                symbol, startTime, endTime)
            .stream()
            .map(SpotMarketDataEntity::getVolume)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateFuturesVolume(String symbol, LocalDateTime startTime, LocalDateTime endTime) {
        return futuresMarketDataRepository.findBySymbol_ExchangeSymbolAndTimestampBetweenOrderByTimestampDesc(
                symbol, startTime, endTime)
            .stream()
            .map(FuturesMarketDataEntity::getVolume)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateOptionsVolume(String symbol, LocalDateTime startTime, LocalDateTime endTime) {
        return optionMarketDataRepository
            .findByInstrument_Series_UnderlyingAssetAndTimestampBetweenOrderByTimestampDesc(
                symbol, startTime, endTime)
            .stream()
            .map(OptionMarketDataEntity::getVolume)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
} 