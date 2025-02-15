package com.entasis.trading.mapper;

import com.entasis.trading.dto.Greeks;
import com.entasis.trading.dto.OptionsMarketData;
import com.entasis.trading.entity.OptionsMarketDataEntity;
import com.entasis.trading.entity.Symbol;
import org.springframework.stereotype.Component;

@Component
public class OptionsMarketDataMapper extends BaseMarketDataMapper<OptionsMarketDataEntity, OptionsMarketData> {
    
    @Override
    public OptionsMarketDataEntity toEntity(OptionsMarketData dto, Symbol symbol) {
        OptionsMarketDataEntity entity = new OptionsMarketDataEntity();
        mapCommonEntityFields(dto, entity, symbol);
        
        entity.setUnderlyingPrice(dto.getUnderlyingPrice());
        entity.setOpenInterest(dto.getOpenInterest());
        entity.setBidPrice(dto.getBidPrice());
        entity.setAskPrice(dto.getAskPrice());
        entity.setImpliedVolatility(dto.getImpliedVolatility());
        entity.setStrikePrice(dto.getStrikePrice());
        entity.setOptionType(dto.getOptionType());
        entity.setExpiryDate(dto.getExpiryDate());
        
        if (dto.getGreeks() != null) {
            entity.setDelta(dto.getGreeks().getDelta());
            entity.setGamma(dto.getGreeks().getGamma());
            entity.setVega(dto.getGreeks().getVega());
            entity.setTheta(dto.getGreeks().getTheta());
            entity.setRho(dto.getGreeks().getRho());
        }
        
        return entity;
    }

    @Override
    public OptionsMarketData toDto(OptionsMarketDataEntity entity) {
        OptionsMarketData dto = new OptionsMarketData();
        mapCommonDtoFields(entity, dto);
        
        dto.setUnderlyingPrice(entity.getUnderlyingPrice());
        dto.setOpenInterest(entity.getOpenInterest());
        dto.setBidPrice(entity.getBidPrice());
        dto.setAskPrice(entity.getAskPrice());
        dto.setImpliedVolatility(entity.getImpliedVolatility());
        dto.setStrikePrice(entity.getStrikePrice());
        dto.setOptionType(entity.getOptionType());
        dto.setExpiryDate(entity.getExpiryDate());
        
        Greeks greeks = new Greeks();
        greeks.setDelta(entity.getDelta());
        greeks.setGamma(entity.getGamma());
        greeks.setVega(entity.getVega());
        greeks.setTheta(entity.getTheta());
        greeks.setRho(entity.getRho());
        dto.setGreeks(greeks);
        
        return dto;
    }
} 