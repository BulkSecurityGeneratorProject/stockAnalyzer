package com.ichoice.stockanalyzer.web.rest.mapper;

import com.ichoice.stockanalyzer.domain.*;
import com.ichoice.stockanalyzer.web.rest.dto.StockCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockCategory and its DTO StockCategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockCategoryMapper {

    StockCategoryDTO stockCategoryToStockCategoryDTO(StockCategory stockCategory);

    StockCategory stockCategoryDTOToStockCategory(StockCategoryDTO stockCategoryDTO);
}
