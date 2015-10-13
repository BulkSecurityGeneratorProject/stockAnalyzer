package com.ichoice.stockanalyzer.repository;

import com.ichoice.stockanalyzer.domain.StockPriceWeekly;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockPriceWeekly entity.
 */
public interface StockPriceWeeklyRepository extends JpaRepository<StockPriceWeekly,Long> {

}
