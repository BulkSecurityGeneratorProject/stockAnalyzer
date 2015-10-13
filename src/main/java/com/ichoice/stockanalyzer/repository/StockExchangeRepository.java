package com.ichoice.stockanalyzer.repository;

import com.ichoice.stockanalyzer.domain.StockExchange;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockExchange entity.
 */
public interface StockExchangeRepository extends JpaRepository<StockExchange,Long> {

}
