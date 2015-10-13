package com.ichoice.stockanalyzer.web.rest.mapper;

import com.ichoice.stockanalyzer.domain.*;
import com.ichoice.stockanalyzer.web.rest.dto.StockInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockInfo and its DTO StockInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockInfoMapper {

    @Mapping(source = "stockExchange.id", target = "stockExchangeId")
    @Mapping(source = "stockExchange.name", target = "stockExchangeName")
    @Mapping(source = "stockCategory.id", target = "stockCategoryId")
    @Mapping(source = "stockCategory.name", target = "stockCategoryName")
    StockInfoDTO stockInfoToStockInfoDTO(StockInfo stockInfo);

    @Mapping(source = "stockExchangeId", target = "stockExchange")
    @Mapping(source = "stockCategoryId", target = "stockCategory")
    StockInfo stockInfoDTOToStockInfo(StockInfoDTO stockInfoDTO);

    default StockExchange stockExchangeFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockExchange stockExchange = new StockExchange();
        stockExchange.setId(id);
        return stockExchange;
    }

    default StockCategory stockCategoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        StockCategory stockCategory = new StockCategory();
        stockCategory.setId(id);
        return stockCategory;
    }
}
