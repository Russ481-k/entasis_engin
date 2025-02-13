package com.entasis.trading.service.impl;

import com.entasis.trading.dto.FuturesMarketData;
import com.entasis.trading.entity.ExchangeEntity;
import com.entasis.trading.entity.SymbolEntity;
import com.entasis.trading.entity.enums.ExchangeStatus;
import com.entasis.trading.entity.enums.ExchangeType;
import com.entasis.trading.mapper.FuturesMarketDataMapper;
import com.entasis.trading.repository.ExchangeRepository;
import com.entasis.trading.repository.FuturesMarketDataRepository;
import com.entasis.trading.repository.SymbolRepository;
import com.entasis.trading.service.FuturesMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FuturesMarketDataServiceImpl implements FuturesMarketDataService {

    private final FuturesMarketDataRepository futuresMarketDataRepository;
    private final ExchangeRepository exchangeRepository;
    private final SymbolRepository symbolRepository;
    private final FuturesMarketDataMapper futuresMarketDataMapper;

    @Override
    @Transactional
    public FuturesMarketData save(FuturesMarketData marketData) {
        ExchangeEntity exchange = getOrCreateExchange(marketData.getExchange());
        SymbolEntity symbol = getOrCreateSymbol(marketData.getSymbol());
        
        var entity = futuresMarketDataMapper.toEntity(marketData, symbol, exchange);
        entity = futuresMarketDataRepository.save(entity);
        return futuresMarketDataMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void saveAll(List<FuturesMarketData> marketDataList) {
        marketDataList.forEach(this::save);
    }

    @Override
    public List<FuturesMarketData> findBySymbolAndExchange(String symbol, String exchange) {
        return futuresMarketDataRepository.findBySymbol_SymbolAndExchange_NameOrderByTimestampDesc(symbol, exchange)
            .stream()
            .map(futuresMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<FuturesMarketData> findBySymbol(String symbol) {
        return futuresMarketDataRepository.findBySymbol_SymbolOrderByTimestampDesc(symbol)
            .stream()
            .map(futuresMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<FuturesMarketData> findBySymbolAndTimeRange(String symbol, LocalDateTime startTime, LocalDateTime endTime) {
        return futuresMarketDataRepository.findBySymbol_SymbolAndTimestampBetweenOrderByTimestampDesc(symbol, startTime, endTime)
            .stream()
            .map(futuresMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<FuturesMarketData> findByExchange(String exchange) {
        return futuresMarketDataRepository.findByExchange_NameOrderByTimestampDesc(exchange)
            .stream()
            .map(futuresMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        futuresMarketDataRepository.deleteById(id);
    }

    @Transactional
    public void saveMarketData(FuturesMarketData data) {
        ExchangeEntity exchange = exchangeRepository.findByNameAndType(data.getExchange(), ExchangeType.FUTURES)
            .orElseThrow(() -> new RuntimeException("Exchange not found"));
        SymbolEntity symbol = getOrCreateSymbol(data.getSymbol());
        
        var entity = futuresMarketDataMapper.toEntity(data, symbol, exchange);
        futuresMarketDataRepository.save(entity);
    }

    private ExchangeEntity getOrCreateExchange(String name) {
        return exchangeRepository.findByNameAndType(name, ExchangeType.FUTURES)
            .orElseGet(() -> {
                ExchangeEntity newExchange = new ExchangeEntity();
                newExchange.setName(name);
                newExchange.setType(ExchangeType.FUTURES);
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
} 