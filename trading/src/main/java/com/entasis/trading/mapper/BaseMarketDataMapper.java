package com.entasis.trading.mapper;

import com.entasis.trading.dto.BaseMarketDataDto;
import com.entasis.trading.entity.BaseMarketDataEntity;
import com.entasis.trading.entity.Symbol;

public abstract class BaseMarketDataMapper<E extends BaseMarketDataEntity, D extends BaseMarketDataDto> {
    
    protected void mapCommonEntityFields(D dto, E entity, Symbol symbol) {
        entity.setSymbol(symbol);
        entity.setPrice(dto.getPrice());
        entity.setVolume(dto.getVolume());
        entity.setTimestamp(dto.getTimestamp());
    }

    protected void mapCommonDtoFields(E entity, D dto) {
        dto.setSymbol(entity.getSymbol().getExchangeSymbol());
        dto.setPrice(entity.getPrice());
        dto.setVolume(entity.getVolume());
        dto.setTimestamp(entity.getTimestamp());
    }

    public abstract E toEntity(D dto, Symbol symbol);
    public abstract D toDto(E entity);
} 