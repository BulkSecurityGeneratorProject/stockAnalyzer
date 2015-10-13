package com.ichoice.stockanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ichoice.stockanalyzer.domain.StockSplit;
import com.ichoice.stockanalyzer.repository.StockSplitRepository;
import com.ichoice.stockanalyzer.web.rest.util.HeaderUtil;
import com.ichoice.stockanalyzer.web.rest.util.PaginationUtil;
import com.ichoice.stockanalyzer.web.rest.dto.StockSplitDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockSplitMapper;
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
 * REST controller for managing StockSplit.
 */
@RestController
@RequestMapping("/api")
public class StockSplitResource {

    private final Logger log = LoggerFactory.getLogger(StockSplitResource.class);

    @Inject
    private StockSplitRepository stockSplitRepository;

    @Inject
    private StockSplitMapper stockSplitMapper;

    /**
     * POST  /stockSplits -> Create a new stockSplit.
     */
    @RequestMapping(value = "/stockSplits",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockSplitDTO> createStockSplit(@Valid @RequestBody StockSplitDTO stockSplitDTO) throws URISyntaxException {
        log.debug("REST request to save StockSplit : {}", stockSplitDTO);
        if (stockSplitDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new stockSplit cannot already have an ID").body(null);
        }
        StockSplit stockSplit = stockSplitMapper.stockSplitDTOToStockSplit(stockSplitDTO);
        StockSplit result = stockSplitRepository.save(stockSplit);
        return ResponseEntity.created(new URI("/api/stockSplits/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("stockSplit", result.getId().toString()))
                .body(stockSplitMapper.stockSplitToStockSplitDTO(result));
    }

    /**
     * PUT  /stockSplits -> Updates an existing stockSplit.
     */
    @RequestMapping(value = "/stockSplits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockSplitDTO> updateStockSplit(@Valid @RequestBody StockSplitDTO stockSplitDTO) throws URISyntaxException {
        log.debug("REST request to update StockSplit : {}", stockSplitDTO);
        if (stockSplitDTO.getId() == null) {
            return createStockSplit(stockSplitDTO);
        }
        StockSplit stockSplit = stockSplitMapper.stockSplitDTOToStockSplit(stockSplitDTO);
        StockSplit result = stockSplitRepository.save(stockSplit);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("stockSplit", stockSplitDTO.getId().toString()))
                .body(stockSplitMapper.stockSplitToStockSplitDTO(result));
    }

    /**
     * GET  /stockSplits -> get all the stockSplits.
     */
    @RequestMapping(value = "/stockSplits",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StockSplitDTO>> getAllStockSplits(Pageable pageable)
        throws URISyntaxException {
        Page<StockSplit> page = stockSplitRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stockSplits");
        return new ResponseEntity<>(page.getContent().stream()
            .map(stockSplitMapper::stockSplitToStockSplitDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /stockSplits/:id -> get the "id" stockSplit.
     */
    @RequestMapping(value = "/stockSplits/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockSplitDTO> getStockSplit(@PathVariable Long id) {
        log.debug("REST request to get StockSplit : {}", id);
        return Optional.ofNullable(stockSplitRepository.findOne(id))
            .map(stockSplitMapper::stockSplitToStockSplitDTO)
            .map(stockSplitDTO -> new ResponseEntity<>(
                stockSplitDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stockSplits/:id -> delete the "id" stockSplit.
     */
    @RequestMapping(value = "/stockSplits/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockSplit(@PathVariable Long id) {
        log.debug("REST request to delete StockSplit : {}", id);
        stockSplitRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockSplit", id.toString())).build();
    }
}
