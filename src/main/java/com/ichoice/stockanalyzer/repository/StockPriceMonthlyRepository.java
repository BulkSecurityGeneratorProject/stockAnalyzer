package com.ichoice.stockanalyzer.repository;

import com.ichoice.stockanalyzer.domain.StockPriceMonthly;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockPriceMonthly entity.
 */
public interface StockPriceMonthlyRepository extends JpaRepository<StockPriceMonthly,Long> {

}
