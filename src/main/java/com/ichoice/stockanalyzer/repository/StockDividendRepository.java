package com.ichoice.stockanalyzer.repository;

import com.ichoice.stockanalyzer.domain.StockDividend;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockDividend entity.
 */
public interface StockDividendRepository extends JpaRepository<StockDividend,Long> {

}
