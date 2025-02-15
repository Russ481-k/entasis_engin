package com.entasis.trading.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import com.entasis.trading.entity.Symbol;
import com.entasis.trading.entity.enums.InstrumentType;
import com.entasis.trading.dto.MarketData;

public abstract class BaseMarketDataService<T extends MarketData> {

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public T save(T marketData) {
        Symbol symbol = getOrCreateSymbol(marketData.getSymbol(), getInstrumentType());
        saveInternal(marketData, symbol);
        return marketData;
    }

    protected abstract InstrumentType getInstrumentType();
    protected abstract void saveInternal(T marketData, Symbol symbol);
    protected abstract Symbol getOrCreateSymbol(String symbolName, InstrumentType instrumentType);
} 