package com.ichoice.stockanalyzer.web.rest.mapper;

import com.ichoice.stockanalyzer.domain.*;
import com.ichoice.stockanalyzer.web.rest.dto.StockSplitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockSplit and its DTO StockSplitDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockSplitMapper {

    @Mapping(source = "stockInfo.id", target = "stockInfoId")
    @Mapping(source = "stockInfo.ticker", target = "stockInfoTicker")
    @Mapping(source = "stockPrice.id", target = "stockPriceId")
    @Mapping(source = "stockPrice.day", target = "stockPriceDay")
    StockSplitDTO stockSplitToStockSplitDTO(StockSplit stockSplit);

    @Mapping(source = "stockInfoId", target = "stockInfo")
    @Mapping(source = "stockPriceId", target = "stockPrice")
    StockSplit stockSplitDTOToStockSplit(StockSplitDTO stockSplitDTO);

    default StockInfo stockInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockInfo stockInfo = new StockInfo();
        stockInfo.setId(id);
        return stockInfo;
    }

    default StockPriceDaily stockPriceDailyFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockPriceDaily stockPriceDaily = new StockPriceDaily();
        stockPriceDaily.setId(id);
        return stockPriceDaily;
    }
}
