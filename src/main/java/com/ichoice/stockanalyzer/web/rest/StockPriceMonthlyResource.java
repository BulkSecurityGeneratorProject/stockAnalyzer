package com.ichoice.stockanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ichoice.stockanalyzer.domain.StockPriceMonthly;
import com.ichoice.stockanalyzer.repository.StockPriceMonthlyRepository;
import com.ichoice.stockanalyzer.web.rest.util.HeaderUtil;
import com.ichoice.stockanalyzer.web.rest.util.PaginationUtil;
import com.ichoice.stockanalyzer.web.rest.dto.StockPriceMonthlyDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockPriceMonthlyMapper;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing StockPriceMonthly.
 */
@RestController
@RequestMapping("/api")
public class StockPriceMonthlyResource {

    private final Logger log = LoggerFactory.getLogger(StockPriceMonthlyResource.class);

    @Inject
    private StockPriceMonthlyRepository stockPriceMonthlyRepository;

    @Inject
    private StockPriceMonthlyMapper stockPriceMonthlyMapper;

    /**
     * POST  /stockPriceMonthlys -> Create a new stockPriceMonthly.
     */
    @RequestMapping(value = "/stockPriceMonthlys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockPriceMonthlyDTO> createStockPriceMonthly(@Valid @RequestBody StockPriceMonthlyDTO stockPriceMonthlyDTO) throws URISyntaxException {
        log.debug("REST request to save StockPriceMonthly : {}", stockPriceMonthlyDTO);
        if (stockPriceMonthlyDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new stockPriceMonthly cannot already have an ID").body(null);
        }
        StockPriceMonthly stockPriceMonthly = stockPriceMonthlyMapper.stockPriceMonthlyDTOToStockPriceMonthly(stockPriceMonthlyDTO);
        StockPriceMonthly result = stockPriceMonthlyRepository.save(stockPriceMonthly);
        return ResponseEntity.created(new URI("/api/stockPriceMonthlys/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("stockPriceMonthly", result.getId().toString()))
                .body(stockPriceMonthlyMapper.stockPriceMonthlyToStockPriceMonthlyDTO(result));
    }

    /**
     * PUT  /stockPriceMonthlys -> Updates an existing stockPriceMonthly.
     */
    @RequestMapping(value = "/stockPriceMonthlys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockPriceMonthlyDTO> updateStockPriceMonthly(@Valid @RequestBody StockPriceMonthlyDTO stockPriceMonthlyDTO) throws URISyntaxException {
        log.debug("REST request to update StockPriceMonthly : {}", stockPriceMonthlyDTO);
        if (stockPriceMonthlyDTO.getId() == null) {
            return createStockPriceMonthly(stockPriceMonthlyDTO);
        }
        StockPriceMonthly stockPriceMonthly = stockPriceMonthlyMapper.stockPriceMonthlyDTOToStockPriceMonthly(stockPriceMonthlyDTO);
        StockPriceMonthly result = stockPriceMonthlyRepository.save(stockPriceMonthly);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("stockPriceMonthly", stockPriceMonthlyDTO.getId().toString()))
                .body(stockPriceMonthlyMapper.stockPriceMonthlyToStockPriceMonthlyDTO(result));
    }

    /**
     * GET  /stockPriceMonthlys -> get all the stockPriceMonthlys.
     */
    @RequestMapping(value = "/stockPriceMonthlys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StockPriceMonthlyDTO>> getAllStockPriceMonthlys(Pageable pageable)
        throws URISyntaxException {
        Page<StockPriceMonthly> page = stockPriceMonthlyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stockPriceMonthlys");
        return new ResponseEntity<>(page.getContent().stream()
            .map(stockPriceMonthlyMapper::stockPriceMonthlyToStockPriceMonthlyDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /stockPriceMonthlys/:id -> get the "id" stockPriceMonthly.
     */
    @RequestMapping(value = "/stockPriceMonthlys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockPriceMonthlyDTO> getStockPriceMonthly(@PathVariable Long id) {
        log.debug("REST request to get StockPriceMonthly : {}", id);
        return Optional.ofNullable(stockPriceMonthlyRepository.findOne(id))
            .map(stockPriceMonthlyMapper::stockPriceMonthlyToStockPriceMonthlyDTO)
            .map(stockPriceMonthlyDTO -> new ResponseEntity<>(
                stockPriceMonthlyDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stockPriceMonthlys/:id -> delete the "id" stockPriceMonthly.
     */
    @RequestMapping(value = "/stockPriceMonthlys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockPriceMonthly(@PathVariable Long id) {
        log.debug("REST request to delete StockPriceMonthly : {}", id);
        stockPriceMonthlyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockPriceMonthly", id.toString())).build();
    }
}
