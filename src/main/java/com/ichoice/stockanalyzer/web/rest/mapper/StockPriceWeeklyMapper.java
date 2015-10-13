package com.ichoice.stockanalyzer.web.rest.mapper;

import com.ichoice.stockanalyzer.domain.*;
import com.ichoice.stockanalyzer.web.rest.dto.StockPriceWeeklyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockPriceWeekly and its DTO StockPriceWeeklyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockPriceWeeklyMapper {

    @Mapping(source = "stockInfo.id", target = "stockInfoId")
    @Mapping(source = "stockInfo.ticker", target = "stockInfoTicker")
    StockPriceWeeklyDTO stockPriceWeeklyToStockPriceWeeklyDTO(StockPriceWeekly stockPriceWeekly);

    @Mapping(source = "stockInfoId", target = "stockInfo")
    StockPriceWeekly stockPriceWeeklyDTOToStockPriceWeekly(StockPriceWeeklyDTO stockPriceWeeklyDTO);

    default StockInfo stockInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockInfo stockInfo = new StockInfo();
        stockInfo.setId(id);
        return stockInfo;
    }
}
