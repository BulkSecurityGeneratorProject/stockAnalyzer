package com.ichoice.stockanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ichoice.stockanalyzer.domain.StockPriceWeekly;
import com.ichoice.stockanalyzer.repository.StockPriceWeeklyRepository;
import com.ichoice.stockanalyzer.web.rest.util.HeaderUtil;
import com.ichoice.stockanalyzer.web.rest.util.PaginationUtil;
import com.ichoice.stockanalyzer.web.rest.dto.StockPriceWeeklyDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockPriceWeeklyMapper;
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
 * REST controller for managing StockPriceWeekly.
 */
@RestController
@RequestMapping("/api")
public class StockPriceWeeklyResource {

    private final Logger log = LoggerFactory.getLogger(StockPriceWeeklyResource.class);

    @Inject
    private StockPriceWeeklyRepository stockPriceWeeklyRepository;

    @Inject
    private StockPriceWeeklyMapper stockPriceWeeklyMapper;

    /**
     * POST  /stockPriceWeeklys -> Create a new stockPriceWeekly.
     */
    @RequestMapping(value = "/stockPriceWeeklys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockPriceWeeklyDTO> createStockPriceWeekly(@Valid @RequestBody StockPriceWeeklyDTO stockPriceWeeklyDTO) throws URISyntaxException {
        log.debug("REST request to save StockPriceWeekly : {}", stockPriceWeeklyDTO);
        if (stockPriceWeeklyDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new stockPriceWeekly cannot already have an ID").body(null);
        }
        StockPriceWeekly stockPriceWeekly = stockPriceWeeklyMapper.stockPriceWeeklyDTOToStockPriceWeekly(stockPriceWeeklyDTO);
        StockPriceWeekly result = stockPriceWeeklyRepository.save(stockPriceWeekly);
        return ResponseEntity.created(new URI("/api/stockPriceWeeklys/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("stockPriceWeekly", result.getId().toString()))
                .body(stockPriceWeeklyMapper.stockPriceWeeklyToStockPriceWeeklyDTO(result));
    }

    /**
     * PUT  /stockPriceWeeklys -> Updates an existing stockPriceWeekly.
     */
    @RequestMapping(value = "/stockPriceWeeklys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockPriceWeeklyDTO> updateStockPriceWeekly(@Valid @RequestBody StockPriceWeeklyDTO stockPriceWeeklyDTO) throws URISyntaxException {
        log.debug("REST request to update StockPriceWeekly : {}", stockPriceWeeklyDTO);
        if (stockPriceWeeklyDTO.getId() == null) {
            return createStockPriceWeekly(stockPriceWeeklyDTO);
        }
        StockPriceWeekly stockPriceWeekly = stockPriceWeeklyMapper.stockPriceWeeklyDTOToStockPriceWeekly(stockPriceWeeklyDTO);
        StockPriceWeekly result = stockPriceWeeklyRepository.save(stockPriceWeekly);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("stockPriceWeekly", stockPriceWeeklyDTO.getId().toString()))
                .body(stockPriceWeeklyMapper.stockPriceWeeklyToStockPriceWeeklyDTO(result));
    }

    /**
     * GET  /stockPriceWeeklys -> get all the stockPriceWeeklys.
     */
    @RequestMapping(value = "/stockPriceWeeklys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StockPriceWeeklyDTO>> getAllStockPriceWeeklys(Pageable pageable)
        throws URISyntaxException {
        Page<StockPriceWeekly> page = stockPriceWeeklyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stockPriceWeeklys");
        return new ResponseEntity<>(page.getContent().stream()
            .map(stockPriceWeeklyMapper::stockPriceWeeklyToStockPriceWeeklyDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /stockPriceWeeklys/:id -> get the "id" stockPriceWeekly.
     */
    @RequestMapping(value = "/stockPriceWeeklys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockPriceWeeklyDTO> getStockPriceWeekly(@PathVariable Long id) {
        log.debug("REST request to get StockPriceWeekly : {}", id);
        return Optional.ofNullable(stockPriceWeeklyRepository.findOne(id))
            .map(stockPriceWeeklyMapper::stockPriceWeeklyToStockPriceWeeklyDTO)
            .map(stockPriceWeeklyDTO -> new ResponseEntity<>(
                stockPriceWeeklyDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stockPriceWeeklys/:id -> delete the "id" stockPriceWeekly.
     */
    @RequestMapping(value = "/stockPriceWeeklys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockPriceWeekly(@PathVariable Long id) {
        log.debug("REST request to delete StockPriceWeekly : {}", id);
        stockPriceWeeklyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockPriceWeekly", id.toString())).build();
    }
}
