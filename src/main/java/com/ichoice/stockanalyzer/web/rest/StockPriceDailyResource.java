package com.ichoice.stockanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ichoice.stockanalyzer.domain.StockPriceDaily;
import com.ichoice.stockanalyzer.repository.StockPriceDailyRepository;
import com.ichoice.stockanalyzer.web.rest.util.HeaderUtil;
import com.ichoice.stockanalyzer.web.rest.util.PaginationUtil;
import com.ichoice.stockanalyzer.web.rest.dto.StockPriceDailyDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockPriceDailyMapper;
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
 * REST controller for managing StockPriceDaily.
 */
@RestController
@RequestMapping("/api")
public class StockPriceDailyResource {

    private final Logger log = LoggerFactory.getLogger(StockPriceDailyResource.class);

    @Inject
    private StockPriceDailyRepository stockPriceDailyRepository;

    @Inject
    private StockPriceDailyMapper stockPriceDailyMapper;

    /**
     * POST  /stockPriceDailys -> Create a new stockPriceDaily.
     */
    @RequestMapping(value = "/stockPriceDailys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockPriceDailyDTO> createStockPriceDaily(@Valid @RequestBody StockPriceDailyDTO stockPriceDailyDTO) throws URISyntaxException {
        log.debug("REST request to save StockPriceDaily : {}", stockPriceDailyDTO);
        if (stockPriceDailyDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new stockPriceDaily cannot already have an ID").body(null);
        }
        StockPriceDaily stockPriceDaily = stockPriceDailyMapper.stockPriceDailyDTOToStockPriceDaily(stockPriceDailyDTO);
        StockPriceDaily result = stockPriceDailyRepository.save(stockPriceDaily);
        return ResponseEntity.created(new URI("/api/stockPriceDailys/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("stockPriceDaily", result.getId().toString()))
                .body(stockPriceDailyMapper.stockPriceDailyToStockPriceDailyDTO(result));
    }

    /**
     * PUT  /stockPriceDailys -> Updates an existing stockPriceDaily.
     */
    @RequestMapping(value = "/stockPriceDailys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockPriceDailyDTO> updateStockPriceDaily(@Valid @RequestBody StockPriceDailyDTO stockPriceDailyDTO) throws URISyntaxException {
        log.debug("REST request to update StockPriceDaily : {}", stockPriceDailyDTO);
        if (stockPriceDailyDTO.getId() == null) {
            return createStockPriceDaily(stockPriceDailyDTO);
        }
        StockPriceDaily stockPriceDaily = stockPriceDailyMapper.stockPriceDailyDTOToStockPriceDaily(stockPriceDailyDTO);
        StockPriceDaily result = stockPriceDailyRepository.save(stockPriceDaily);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("stockPriceDaily", stockPriceDailyDTO.getId().toString()))
                .body(stockPriceDailyMapper.stockPriceDailyToStockPriceDailyDTO(result));
    }

    /**
     * GET  /stockPriceDailys -> get all the stockPriceDailys.
     */
    @RequestMapping(value = "/stockPriceDailys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StockPriceDailyDTO>> getAllStockPriceDailys(Pageable pageable)
        throws URISyntaxException {
        Page<StockPriceDaily> page = stockPriceDailyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stockPriceDailys");
        return new ResponseEntity<>(page.getContent().stream()
            .map(stockPriceDailyMapper::stockPriceDailyToStockPriceDailyDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /stockPriceDailys/:id -> get the "id" stockPriceDaily.
     */
    @RequestMapping(value = "/stockPriceDailys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockPriceDailyDTO> getStockPriceDaily(@PathVariable Long id) {
        log.debug("REST request to get StockPriceDaily : {}", id);
        return Optional.ofNullable(stockPriceDailyRepository.findOne(id))
            .map(stockPriceDailyMapper::stockPriceDailyToStockPriceDailyDTO)
            .map(stockPriceDailyDTO -> new ResponseEntity<>(
                stockPriceDailyDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stockPriceDailys/:id -> delete the "id" stockPriceDaily.
     */
    @RequestMapping(value = "/stockPriceDailys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockPriceDaily(@PathVariable Long id) {
        log.debug("REST request to delete StockPriceDaily : {}", id);
        stockPriceDailyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockPriceDaily", id.toString())).build();
    }
}
