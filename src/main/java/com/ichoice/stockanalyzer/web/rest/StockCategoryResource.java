package com.ichoice.stockanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ichoice.stockanalyzer.domain.StockCategory;
import com.ichoice.stockanalyzer.repository.StockCategoryRepository;
import com.ichoice.stockanalyzer.web.rest.util.HeaderUtil;
import com.ichoice.stockanalyzer.web.rest.util.PaginationUtil;
import com.ichoice.stockanalyzer.web.rest.dto.StockCategoryDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing StockCategory.
 */
@RestController
@RequestMapping("/api")
public class StockCategoryResource {

    private final Logger log = LoggerFactory.getLogger(StockCategoryResource.class);

    @Inject
    private StockCategoryRepository stockCategoryRepository;

    @Inject
    private StockCategoryMapper stockCategoryMapper;

    /**
     * POST  /stockCategorys -> Create a new stockCategory.
     */
    @RequestMapping(value = "/stockCategorys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockCategoryDTO> createStockCategory(@RequestBody StockCategoryDTO stockCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save StockCategory : {}", stockCategoryDTO);
        if (stockCategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new stockCategory cannot already have an ID").body(null);
        }
        StockCategory stockCategory = stockCategoryMapper.stockCategoryDTOToStockCategory(stockCategoryDTO);
        StockCategory result = stockCategoryRepository.save(stockCategory);
        return ResponseEntity.created(new URI("/api/stockCategorys/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("stockCategory", result.getId().toString()))
                .body(stockCategoryMapper.stockCategoryToStockCategoryDTO(result));
    }

    /**
     * PUT  /stockCategorys -> Updates an existing stockCategory.
     */
    @RequestMapping(value = "/stockCategorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockCategoryDTO> updateStockCategory(@RequestBody StockCategoryDTO stockCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update StockCategory : {}", stockCategoryDTO);
        if (stockCategoryDTO.getId() == null) {
            return createStockCategory(stockCategoryDTO);
        }
        StockCategory stockCategory = stockCategoryMapper.stockCategoryDTOToStockCategory(stockCategoryDTO);
        StockCategory result = stockCategoryRepository.save(stockCategory);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("stockCategory", stockCategoryDTO.getId().toString()))
                .body(stockCategoryMapper.stockCategoryToStockCategoryDTO(result));
    }

    /**
     * GET  /stockCategorys -> get all the stockCategorys.
     */
    @RequestMapping(value = "/stockCategorys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StockCategoryDTO>> getAllStockCategorys(Pageable pageable)
        throws URISyntaxException {
        Page<StockCategory> page = stockCategoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stockCategorys");
        return new ResponseEntity<>(page.getContent().stream()
            .map(stockCategoryMapper::stockCategoryToStockCategoryDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /stockCategorys/:id -> get the "id" stockCategory.
     */
    @RequestMapping(value = "/stockCategorys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockCategoryDTO> getStockCategory(@PathVariable Long id) {
        log.debug("REST request to get StockCategory : {}", id);
        return Optional.ofNullable(stockCategoryRepository.findOne(id))
            .map(stockCategoryMapper::stockCategoryToStockCategoryDTO)
            .map(stockCategoryDTO -> new ResponseEntity<>(
                stockCategoryDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stockCategorys/:id -> delete the "id" stockCategory.
     */
    @RequestMapping(value = "/stockCategorys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockCategory(@PathVariable Long id) {
        log.debug("REST request to delete StockCategory : {}", id);
        stockCategoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockCategory", id.toString())).build();
    }
}
