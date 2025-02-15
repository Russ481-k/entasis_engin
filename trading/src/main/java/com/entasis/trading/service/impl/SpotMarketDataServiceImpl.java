package com.entasis.trading.service.impl;

import com.entasis.trading.dto.SpotMarketData;
import com.entasis.trading.entity.SpotMarketDataEntity;
import com.entasis.trading.entity.Symbol;
import com.entasis.trading.entity.enums.InstrumentType;
import com.entasis.trading.repository.SpotMarketDataRepository;
import com.entasis.trading.repository.SymbolRepository;
import com.entasis.trading.repository.ExchangeRepository;
import com.entasis.trading.service.SpotMarketDataService;
import com.entasis.trading.mapper.SpotMarketDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entasis.trading.entity.ExchangeEntity;
import com.entasis.trading.entity.enums.ExchangeType;
import com.entasis.trading.service.BaseMarketDataService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class SpotMarketDataServiceImpl extends BaseMarketDataService<SpotMarketData> 
    implements SpotMarketDataService {

    private final SpotMarketDataRepository spotMarketDataRepository;
    private final ExchangeRepository exchangeRepository;
    private final SpotMarketDataMapper spotMarketDataMapper;
    private final SymbolRepository symbolRepository;

    public SpotMarketDataServiceImpl(
        SymbolRepository symbolRepository,
        ExchangeRepository exchangeRepository,
        SpotMarketDataRepository spotMarketDataRepository,
        SpotMarketDataMapper spotMarketDataMapper
    ) {
        this.spotMarketDataRepository = spotMarketDataRepository;
        this.exchangeRepository = exchangeRepository;
        this.spotMarketDataMapper = spotMarketDataMapper;
        this.symbolRepository = symbolRepository;
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.SPOT;
    }

    @Override
    protected void saveInternal(SpotMarketData marketData, Symbol symbol) {
        SpotMarketDataEntity entity = spotMarketDataMapper.toEntity(marketData, symbol);
        spotMarketDataRepository.save(entity);
    }

    @Override
    public List<SpotMarketData> findBySymbolAndExchange(String symbol, String exchange) {
        return spotMarketDataRepository.findBySymbol_ExchangeSymbolAndSymbol_Exchange_NameOrderByTimestampDesc(
            symbol, exchange)
            .stream()
            .map(spotMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<SpotMarketData> findBySymbol(String symbol) {
        return spotMarketDataRepository.findBySymbol_ExchangeSymbolOrderByTimestampDesc(symbol)
            .stream()
            .map(spotMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<SpotMarketData> findBySymbolAndTimeRange(String symbol, LocalDateTime startTime, LocalDateTime endTime) {
        return spotMarketDataRepository.findBySymbol_ExchangeSymbolAndTimestampBetweenOrderByTimestampDesc(
            symbol, startTime, endTime)
            .stream()
            .map(spotMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<SpotMarketData> findByExchange(String exchange) {
        return spotMarketDataRepository.findBySymbol_Exchange_NameOrderByTimestampDesc(exchange)
            .stream()
            .map(spotMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        spotMarketDataRepository.deleteById(id);
    }

    @Override
    public Symbol getOrCreateSymbol(String symbolName, InstrumentType instrumentType) {
        return symbolRepository.findByExchangeSymbolAndInstrumentTypeAndExchange_Type(
            symbolName, instrumentType, ExchangeType.SPOT)
            .orElseGet(() -> {
                ExchangeEntity exchange = exchangeRepository.findByNameAndType("binance", ExchangeType.valueOf(instrumentType.name()))
                    .orElseThrow(() -> new RuntimeException(
                        String.format("Exchange not found: binance/%s", instrumentType)));
                
                Symbol newSymbol = new Symbol();
                newSymbol.setExchange(exchange);
                newSymbol.setExchangeSymbol(symbolName);
                String[] parts = symbolName.split("USDT");
                newSymbol.setBaseAsset(parts[0]);
                newSymbol.setQuoteAsset("USDT");
                newSymbol.setInstrumentType(instrumentType);
                
                return symbolRepository.save(newSymbol);
            });
    }

    @Override
    @Transactional
    public SpotMarketData save(SpotMarketData marketData) {
        return super.save(marketData);
    }

    @Override
    @Transactional
    public void saveAll(List<SpotMarketData> marketDataList) {
        marketDataList.forEach(this::save);
    }
} 