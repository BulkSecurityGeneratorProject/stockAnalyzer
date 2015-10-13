package com.ichoice.stockanalyzer.repository;

import com.ichoice.stockanalyzer.domain.StockCategory;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockCategory entity.
 */
public interface StockCategoryRepository extends JpaRepository<StockCategory,Long> {

}
