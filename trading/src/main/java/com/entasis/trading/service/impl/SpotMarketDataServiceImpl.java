package com.entasis.trading.service.impl;

import com.entasis.trading.dto.SpotMarketData;
import com.entasis.trading.entity.ExchangeEntity;
import com.entasis.trading.entity.SymbolEntity;
import com.entasis.trading.entity.enums.ExchangeStatus;
import com.entasis.trading.entity.enums.ExchangeType;
import com.entasis.trading.mapper.SpotMarketDataMapper;
import com.entasis.trading.repository.ExchangeRepository;
import com.entasis.trading.repository.SpotMarketDataRepository;
import com.entasis.trading.repository.SymbolRepository;
import com.entasis.trading.service.SpotMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpotMarketDataServiceImpl implements SpotMarketDataService {

    private final SpotMarketDataRepository spotMarketDataRepository;
    private final ExchangeRepository exchangeRepository;
    private final SymbolRepository symbolRepository;
    private final SpotMarketDataMapper spotMarketDataMapper;

    @Override
    @Transactional
    public SpotMarketData save(SpotMarketData marketData) {
        ExchangeEntity exchange = getOrCreateExchange(marketData.getExchange());
        SymbolEntity symbol = getOrCreateSymbol(marketData.getSymbol());
        
        var entity = spotMarketDataMapper.toEntity(marketData, symbol, exchange);
        entity = spotMarketDataRepository.save(entity);
        return spotMarketDataMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void saveAll(List<SpotMarketData> marketDataList) {
        marketDataList.forEach(this::save);
    }

    @Override
    public List<SpotMarketData> findBySymbolAndExchange(String symbol, String exchange) {
        return spotMarketDataRepository.findBySymbol_SymbolAndExchange_NameOrderByTimestampDesc(symbol, exchange)
            .stream()
            .map(spotMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<SpotMarketData> findBySymbol(String symbol) {
        return spotMarketDataRepository.findBySymbol_SymbolOrderByTimestampDesc(symbol)
            .stream()
            .map(spotMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<SpotMarketData> findBySymbolAndTimeRange(String symbol, LocalDateTime startTime, LocalDateTime endTime) {
        return spotMarketDataRepository.findBySymbol_SymbolAndTimestampBetweenOrderByTimestampDesc(symbol, startTime, endTime)
            .stream()
            .map(spotMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<SpotMarketData> findByExchange(String exchange) {
        return spotMarketDataRepository.findByExchange_NameOrderByTimestampDesc(exchange)
            .stream()
            .map(spotMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        spotMarketDataRepository.deleteById(id);
    }

    private ExchangeEntity getOrCreateExchange(String name) {
        return exchangeRepository.findByNameAndType(name, ExchangeType.SPOT)
            .orElseGet(() -> {
                ExchangeEntity newExchange = new ExchangeEntity();
                newExchange.setName(name);
                newExchange.setType(ExchangeType.SPOT);
                newExchange.setStatus(ExchangeStatus.ACTIVE);
                return exchangeRepository.save(newExchange);
            });
    }

    private SymbolEntity getOrCreateSymbol(String symbolName) {
        return symbolRepository.findBySymbol(symbolName)
            .orElseGet(() -> {
                SymbolEntity newSymbol = new SymbolEntity();
                newSymbol.setSymbol(symbolName);
                String[] parts = symbolName.split("/");
                newSymbol.setBaseAsset(parts[0]);
                newSymbol.setQuoteAsset(parts.length > 1 ? parts[1] : "USDT");
                return symbolRepository.save(newSymbol);
            });
    }

    @Transactional
    public void saveMarketData(SpotMarketData data) {
        ExchangeEntity exchange = exchangeRepository.findByNameAndType(data.getExchange(), ExchangeType.SPOT)
            .orElseThrow(() -> new RuntimeException("Exchange not found"));
        SymbolEntity symbol = getOrCreateSymbol(data.getSymbol());
        
        var entity = spotMarketDataMapper.toEntity(data, symbol, exchange);
        spotMarketDataRepository.save(entity);
    }
} 