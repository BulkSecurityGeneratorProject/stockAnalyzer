package com.ichoice.stockanalyzer.repository;

import com.ichoice.stockanalyzer.domain.StockSplit;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockSplit entity.
 */
public interface StockSplitRepository extends JpaRepository<StockSplit,Long> {

}
