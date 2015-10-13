package com.ichoice.stockanalyzer.web.rest.mapper;

import com.ichoice.stockanalyzer.domain.*;
import com.ichoice.stockanalyzer.web.rest.dto.StockExchangeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockExchange and its DTO StockExchangeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StockExchangeMapper {

    StockExchangeDTO stockExchangeToStockExchangeDTO(StockExchange stockExchange);

    StockExchange stockExchangeDTOToStockExchange(StockExchangeDTO stockExchangeDTO);
}
