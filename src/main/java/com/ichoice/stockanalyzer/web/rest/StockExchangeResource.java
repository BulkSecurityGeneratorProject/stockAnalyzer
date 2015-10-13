package com.ichoice.stockanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ichoice.stockanalyzer.domain.StockExchange;
import com.ichoice.stockanalyzer.repository.StockExchangeRepository;
import com.ichoice.stockanalyzer.web.rest.util.HeaderUtil;
import com.ichoice.stockanalyzer.web.rest.util.PaginationUtil;
import com.ichoice.stockanalyzer.web.rest.dto.StockExchangeDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockExchangeMapper;
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
 * REST controller for managing StockExchange.
 */
@RestController
@RequestMapping("/api")
public class StockExchangeResource {

    private final Logger log = LoggerFactory.getLogger(StockExchangeResource.class);

    @Inject
    private StockExchangeRepository stockExchangeRepository;

    @Inject
    private StockExchangeMapper stockExchangeMapper;

    /**
     * POST  /stockExchanges -> Create a new stockExchange.
     */
    @RequestMapping(value = "/stockExchanges",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockExchangeDTO> createStockExchange(@RequestBody StockExchangeDTO stockExchangeDTO) throws URISyntaxException {
        log.debug("REST request to save StockExchange : {}", stockExchangeDTO);
        if (stockExchangeDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new stockExchange cannot already have an ID").body(null);
        }
        StockExchange stockExchange = stockExchangeMapper.stockExchangeDTOToStockExchange(stockExchangeDTO);
        StockExchange result = stockExchangeRepository.save(stockExchange);
        return ResponseEntity.created(new URI("/api/stockExchanges/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("stockExchange", result.getId().toString()))
                .body(stockExchangeMapper.stockExchangeToStockExchangeDTO(result));
    }

    /**
     * PUT  /stockExchanges -> Updates an existing stockExchange.
     */
    @RequestMapping(value = "/stockExchanges",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockExchangeDTO> updateStockExchange(@RequestBody StockExchangeDTO stockExchangeDTO) throws URISyntaxException {
        log.debug("REST request to update StockExchange : {}", stockExchangeDTO);
        if (stockExchangeDTO.getId() == null) {
            return createStockExchange(stockExchangeDTO);
        }
        StockExchange stockExchange = stockExchangeMapper.stockExchangeDTOToStockExchange(stockExchangeDTO);
        StockExchange result = stockExchangeRepository.save(stockExchange);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("stockExchange", stockExchangeDTO.getId().toString()))
                .body(stockExchangeMapper.stockExchangeToStockExchangeDTO(result));
    }

    /**
     * GET  /stockExchanges -> get all the stockExchanges.
     */
    @RequestMapping(value = "/stockExchanges",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StockExchangeDTO>> getAllStockExchanges(Pageable pageable)
        throws URISyntaxException {
        Page<StockExchange> page = stockExchangeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stockExchanges");
        return new ResponseEntity<>(page.getContent().stream()
            .map(stockExchangeMapper::stockExchangeToStockExchangeDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /stockExchanges/:id -> get the "id" stockExchange.
     */
    @RequestMapping(value = "/stockExchanges/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockExchangeDTO> getStockExchange(@PathVariable Long id) {
        log.debug("REST request to get StockExchange : {}", id);
        return Optional.ofNullable(stockExchangeRepository.findOne(id))
            .map(stockExchangeMapper::stockExchangeToStockExchangeDTO)
            .map(stockExchangeDTO -> new ResponseEntity<>(
                stockExchangeDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stockExchanges/:id -> delete the "id" stockExchange.
     */
    @RequestMapping(value = "/stockExchanges/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockExchange(@PathVariable Long id) {
        log.debug("REST request to delete StockExchange : {}", id);
        stockExchangeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockExchange", id.toString())).build();
    }
}
