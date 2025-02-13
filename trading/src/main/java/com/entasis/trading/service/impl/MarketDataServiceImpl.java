package com.entasis.trading.service.impl;

import com.entasis.trading.dto.MarketData;
import com.entasis.trading.entity.MarketDataEntity;
import com.entasis.trading.mapper.MarketDataMapper;
import com.entasis.trading.repository.MarketDataRepository;
import com.entasis.trading.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketDataServiceImpl implements MarketDataService {

    private final MarketDataRepository marketDataRepository;
    private final MarketDataMapper marketDataMapper;

    @Override
    @Transactional
    public MarketData save(MarketData marketData) {
        MarketDataEntity entity = marketDataMapper.toEntity(marketData);
        entity = marketDataRepository.save(entity);
        return marketDataMapper.toDto(entity);
    }

    @Override
    public List<MarketData> findBySymbol(String symbol) {
        return marketDataRepository.findBySymbolOrderByTimestampDesc(symbol)
            .stream()
            .map(marketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<MarketData> findBySymbolAndTimeRange(String symbol, LocalDateTime startTime, LocalDateTime endTime) {
        return marketDataRepository.findBySymbolAndTimeRange(symbol, startTime, endTime)
            .stream()
            .map(marketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<MarketData> findByExchange(String exchange) {
        return marketDataRepository.findByExchangeOrderByTimestampDesc(exchange)
            .stream()
            .map(marketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        marketDataRepository.deleteById(id);
    }
} 