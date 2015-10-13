package com.ichoice.stockanalyzer.repository;

import com.ichoice.stockanalyzer.domain.StockInfo;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockInfo entity.
 */
public interface StockInfoRepository extends JpaRepository<StockInfo,Long> {

}
