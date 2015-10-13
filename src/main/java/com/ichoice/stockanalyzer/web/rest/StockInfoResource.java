package com.ichoice.stockanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ichoice.stockanalyzer.domain.StockInfo;
import com.ichoice.stockanalyzer.repository.StockInfoRepository;
import com.ichoice.stockanalyzer.web.rest.util.HeaderUtil;
import com.ichoice.stockanalyzer.web.rest.util.PaginationUtil;
import com.ichoice.stockanalyzer.web.rest.dto.StockInfoDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockInfoMapper;
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
 * REST controller for managing StockInfo.
 */
@RestController
@RequestMapping("/api")
public class StockInfoResource {

    private final Logger log = LoggerFactory.getLogger(StockInfoResource.class);

    @Inject
    private StockInfoRepository stockInfoRepository;

    @Inject
    private StockInfoMapper stockInfoMapper;

    /**
     * POST  /stockInfos -> Create a new stockInfo.
     */
    @RequestMapping(value = "/stockInfos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockInfoDTO> createStockInfo(@RequestBody StockInfoDTO stockInfoDTO) throws URISyntaxException {
        log.debug("REST request to save StockInfo : {}", stockInfoDTO);
        if (stockInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new stockInfo cannot already have an ID").body(null);
        }
        StockInfo stockInfo = stockInfoMapper.stockInfoDTOToStockInfo(stockInfoDTO);
        StockInfo result = stockInfoRepository.save(stockInfo);
        return ResponseEntity.created(new URI("/api/stockInfos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("stockInfo", result.getId().toString()))
                .body(stockInfoMapper.stockInfoToStockInfoDTO(result));
    }

    /**
     * PUT  /stockInfos -> Updates an existing stockInfo.
     */
    @RequestMapping(value = "/stockInfos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockInfoDTO> updateStockInfo(@RequestBody StockInfoDTO stockInfoDTO) throws URISyntaxException {
        log.debug("REST request to update StockInfo : {}", stockInfoDTO);
        if (stockInfoDTO.getId() == null) {
            return createStockInfo(stockInfoDTO);
        }
        StockInfo stockInfo = stockInfoMapper.stockInfoDTOToStockInfo(stockInfoDTO);
        StockInfo result = stockInfoRepository.save(stockInfo);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("stockInfo", stockInfoDTO.getId().toString()))
                .body(stockInfoMapper.stockInfoToStockInfoDTO(result));
    }

    /**
     * GET  /stockInfos -> get all the stockInfos.
     */
    @RequestMapping(value = "/stockInfos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<StockInfoDTO>> getAllStockInfos(Pageable pageable)
        throws URISyntaxException {
        Page<StockInfo> page = stockInfoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stockInfos");
        return new ResponseEntity<>(page.getContent().stream()
            .map(stockInfoMapper::stockInfoToStockInfoDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /stockInfos/:id -> get the "id" stockInfo.
     */
    @RequestMapping(value = "/stockInfos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockInfoDTO> getStockInfo(@PathVariable Long id) {
        log.debug("REST request to get StockInfo : {}", id);
        return Optional.ofNullable(stockInfoRepository.findOne(id))
            .map(stockInfoMapper::stockInfoToStockInfoDTO)
            .map(stockInfoDTO -> new ResponseEntity<>(
                stockInfoDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stockInfos/:id -> delete the "id" stockInfo.
     */
    @RequestMapping(value = "/stockInfos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockInfo(@PathVariable Long id) {
        log.debug("REST request to delete StockInfo : {}", id);
        stockInfoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockInfo", id.toString())).build();
    }
}
