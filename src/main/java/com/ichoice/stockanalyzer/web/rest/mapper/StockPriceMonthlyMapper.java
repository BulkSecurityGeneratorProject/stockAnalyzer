package com.ichoice.stockanalyzer.web.rest.mapper;

import com.ichoice.stockanalyzer.domain.*;
import com.ichoice.stockanalyzer.web.rest.dto.StockPriceMonthlyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockPriceMonthly and its DTO StockPriceMonthlyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockPriceMonthlyMapper {

    @Mapping(source = "stockInfo.id", target = "stockInfoId")
    @Mapping(source = "stockInfo.ticker", target = "stockInfoTicker")
    StockPriceMonthlyDTO stockPriceMonthlyToStockPriceMonthlyDTO(StockPriceMonthly stockPriceMonthly);

    @Mapping(source = "stockInfoId", target = "stockInfo")
    StockPriceMonthly stockPriceMonthlyDTOToStockPriceMonthly(StockPriceMonthlyDTO stockPriceMonthlyDTO);

    default StockInfo stockInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockInfo stockInfo = new StockInfo();
        stockInfo.setId(id);
        return stockInfo;
    }
}
