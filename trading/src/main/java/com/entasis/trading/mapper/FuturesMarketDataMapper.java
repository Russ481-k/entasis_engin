package com.entasis.trading.mapper;

import com.entasis.trading.dto.FuturesMarketData;
import com.entasis.trading.entity.ExchangeEntity;
import com.entasis.trading.entity.FuturesMarketDataEntity;
import com.entasis.trading.entity.Symbol;
import org.springframework.stereotype.Component;

@Component
public class FuturesMarketDataMapper extends BaseMarketDataMapper<FuturesMarketDataEntity, FuturesMarketData> {
    
    @Override
    public FuturesMarketDataEntity toEntity(FuturesMarketData dto, Symbol symbol) {
        FuturesMarketDataEntity entity = new FuturesMarketDataEntity();
        mapCommonEntityFields(dto, entity, symbol);
        entity.setExchange(symbol.getExchange());
        
        entity.setFundingRate(dto.getFundingRate());
        entity.setMarkPrice(dto.getMarkPrice());
        entity.setIndexPrice(dto.getIndexPrice());
        entity.setOpenInterest(dto.getOpenInterest());
        
        return entity;
    }
    
    @Override
    public FuturesMarketData toDto(FuturesMarketDataEntity entity) {
        FuturesMarketData dto = new FuturesMarketData();
        mapCommonDtoFields(entity, dto);
        dto.setOpenInterest(entity.getOpenInterest());
        dto.setFundingRate(entity.getFundingRate());
        dto.setMarkPrice(entity.getMarkPrice());
        dto.setIndexPrice(entity.getIndexPrice());
        return dto;
    }
} 