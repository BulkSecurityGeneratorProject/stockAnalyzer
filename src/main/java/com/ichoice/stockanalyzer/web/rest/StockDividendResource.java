package com.ichoice.stockanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ichoice.stockanalyzer.domain.StockDividend;
import com.ichoice.stockanalyzer.repository.StockDividendRepository;
import com.ichoice.stockanalyzer.web.rest.util.HeaderUtil;
import com.ichoice.stockanalyzer.web.rest.util.PaginationUtil;
import com.ichoice.stockanalyzer.web.rest.dto.StockDividendDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockDividendMapper;
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
 * REST controller for managing StockDividend.
 */
@RestController
@RequestMapping("/api")
public class StockDividendResource {

    private final Logger log = LoggerFactory.getLogger(StockDividendResource.class);

    @Inject
    private StockDividendRepository stockDividendRepository;

    @Inject
    private StockDividendMapper stockDividendMapper;

    /**
     * POST  /stockDividends -> Create a new stockDividend.
     */
    @RequestMapping(value = "/stockDividends",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockDividendDTO> createStockDividend(@Valid @RequestBody StockDividendDTO stockDividendDTO) throws URISyntaxException {
        log.debug("REST request to save StockDividend : {}", stockDividendDTO);
        if (stockDividendDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new stockDividend cannot already have an ID").body(null);
        }
        StockDividend stockDividend = stockDividendMapper.stockDividendDTOToStockDividend(stockDividendDTO);
        StockDividend result = stockDividendRepository.save(stockDividend);
        return ResponseEntity.created(new URI("/api/stockDividends/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("stockDividend", result.getId().toString()))
                .body(stockDividendMapper.stockDividendToStockDividendDTO(result));
    }

    /**
     * PUT  /stockDividends -> Updates an existing stockDividend.
     */
    @RequestMapping(value = "/stockDividends",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockDividendDTO> updateStockDividend(@Valid @RequestBody StockDividendDTO stockDividendDTO) throws URISyntaxException {
        log.debug("REST request to update StockDividend : {}", stockDividendDTO);
        if (stockDividendDTO.getId() == null) {
            return createStockDividend(stockDividendDTO);
        }
        StockDividend stockDividend = stockDividendMapper.stockDividendDTOToStockDividend(stockDividendDTO);
        StockDividend result = stockDividendRepository.save(stockDividend);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("stockDividend", stockDividendDTO.getId().toString()))
                .body(stockDividendMapper.stockDividendToStockDividendDTO(result));
    }

    /**
     * GET  /stockDividends -> get all the stockDividends.
     */
    @RequestMapping(value = "/stockDividends",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StockDividendDTO>> getAllStockDividends(Pageable pageable)
        throws URISyntaxException {
        Page<StockDividend> page = stockDividendRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stockDividends");
        return new ResponseEntity<>(page.getContent().stream()
            .map(stockDividendMapper::stockDividendToStockDividendDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /stockDividends/:id -> get the "id" stockDividend.
     */
    @RequestMapping(value = "/stockDividends/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockDividendDTO> getStockDividend(@PathVariable Long id) {
        log.debug("REST request to get StockDividend : {}", id);
        return Optional.ofNullable(stockDividendRepository.findOne(id))
            .map(stockDividendMapper::stockDividendToStockDividendDTO)
            .map(stockDividendDTO -> new ResponseEntity<>(
                stockDividendDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stockDividends/:id -> delete the "id" stockDividend.
     */
    @RequestMapping(value = "/stockDividends/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockDividend(@PathVariable Long id) {
        log.debug("REST request to delete StockDividend : {}", id);
        stockDividendRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockDividend", id.toString())).build();
    }
}
