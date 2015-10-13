package com.ichoice.stockanalyzer.web.rest.mapper;

import com.ichoice.stockanalyzer.domain.*;
import com.ichoice.stockanalyzer.web.rest.dto.StockPriceDailyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockPriceDaily and its DTO StockPriceDailyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockPriceDailyMapper {

    @Mapping(source = "stockInfo.id", target = "stockInfoId")
    @Mapping(source = "stockInfo.ticker", target = "stockInfoTicker")
    StockPriceDailyDTO stockPriceDailyToStockPriceDailyDTO(StockPriceDaily stockPriceDaily);

    @Mapping(source = "stockInfoId", target = "stockInfo")
    StockPriceDaily stockPriceDailyDTOToStockPriceDaily(StockPriceDailyDTO stockPriceDailyDTO);

    default StockInfo stockInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockInfo stockInfo = new StockInfo();
        stockInfo.setId(id);
        return stockInfo;
    }
}
