package com.ichoice.stockanalyzer.web.rest.mapper;

import com.ichoice.stockanalyzer.domain.*;
import com.ichoice.stockanalyzer.web.rest.dto.StockDividendDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockDividend and its DTO StockDividendDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockDividendMapper {

    @Mapping(source = "stockInfo.id", target = "stockInfoId")
    @Mapping(source = "stockInfo.ticker", target = "stockInfoTicker")
    @Mapping(source = "stockPrice.id", target = "stockPriceId")
    @Mapping(source = "stockPrice.day", target = "stockPriceDay")
    StockDividendDTO stockDividendToStockDividendDTO(StockDividend stockDividend);

    @Mapping(source = "stockInfoId", target = "stockInfo")
    @Mapping(source = "stockPriceId", target = "stockPrice")
    StockDividend stockDividendDTOToStockDividend(StockDividendDTO stockDividendDTO);

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
