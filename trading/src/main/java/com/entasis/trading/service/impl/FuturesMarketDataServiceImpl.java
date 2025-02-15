package com.entasis.trading.service.impl;

import com.entasis.trading.dto.FuturesMarketData;
import com.entasis.trading.entity.FuturesMarketDataEntity;
import com.entasis.trading.entity.Symbol;
import com.entasis.trading.entity.ExchangeEntity;
import com.entasis.trading.entity.enums.ExchangeStatus;
import com.entasis.trading.entity.enums.ExchangeType;
import com.entasis.trading.entity.enums.InstrumentType;
import com.entasis.trading.mapper.FuturesMarketDataMapper;
import com.entasis.trading.repository.FuturesMarketDataRepository;
import com.entasis.trading.repository.SymbolRepository;
import com.entasis.trading.repository.ExchangeRepository;
import com.entasis.trading.service.FuturesMarketDataService;
import com.entasis.trading.service.BaseMarketDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FuturesMarketDataServiceImpl extends BaseMarketDataService<FuturesMarketData> 
    implements FuturesMarketDataService {

    private final SymbolRepository symbolRepository;
    private final ExchangeRepository exchangeRepository;
    private final FuturesMarketDataRepository futuresMarketDataRepository;
    private final FuturesMarketDataMapper futuresMarketDataMapper;

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.FUTURES;
    }

    @Override
    @Transactional
    public FuturesMarketData save(FuturesMarketData marketData) {
        super.save(marketData);
        return marketData;
    }

    @Override
    @Transactional
    public void saveAll(List<FuturesMarketData> marketDataList) {
        marketDataList.forEach(this::save);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    protected void saveInternal(FuturesMarketData marketData, Symbol symbol) {
        try {
            FuturesMarketDataEntity entity = futuresMarketDataMapper.toEntity(marketData, symbol);
            entity.setExchange(symbol.getExchange());
            futuresMarketDataRepository.saveAndFlush(entity);
        } catch (Exception e) {
            log.error("Error saving futures market data: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<FuturesMarketData> findBySymbolAndExchange(String symbol, String exchange) {
        return futuresMarketDataRepository.findBySymbol_ExchangeSymbolAndExchange_NameOrderByTimestampDesc(
            symbol, exchange)
            .stream()
            .map(futuresMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<FuturesMarketData> findBySymbol(String symbol) {
        return futuresMarketDataRepository.findBySymbol_ExchangeSymbolOrderByTimestampDesc(symbol)
            .stream()
            .map(futuresMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<FuturesMarketData> findBySymbolAndTimeRange(String symbol, LocalDateTime startTime, LocalDateTime endTime) {
        return futuresMarketDataRepository.findBySymbol_ExchangeSymbolAndTimestampBetweenOrderByTimestampDesc(
            symbol, startTime, endTime)
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

    @Override
    public Symbol getOrCreateSymbol(String symbolName, InstrumentType instrumentType) {
        return symbolRepository.findByExchangeSymbolAndInstrumentTypeAndExchange_Type(
            symbolName, instrumentType, ExchangeType.FUTURES)
            .orElseGet(() -> {
                ExchangeEntity exchange = exchangeRepository.findByNameAndType("binance", 
                    ExchangeType.valueOf(instrumentType.name()))
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
} 