package com.ichoice.stockanalyzer.repository;

import com.ichoice.stockanalyzer.domain.StockPriceDaily;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockPriceDaily entity.
 */
public interface StockPriceDailyRepository extends JpaRepository<StockPriceDaily,Long> {

}
