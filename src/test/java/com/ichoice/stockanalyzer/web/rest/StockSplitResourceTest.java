package com.ichoice.stockanalyzer.web.rest;

import com.ichoice.stockanalyzer.Application;
import com.ichoice.stockanalyzer.domain.StockSplit;
import com.ichoice.stockanalyzer.repository.StockSplitRepository;
import com.ichoice.stockanalyzer.web.rest.dto.StockSplitDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockSplitMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StockSplitResource REST controller.
 *
 * @see StockSplitResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StockSplitResourceTest {


    private static final Long DEFAULT_DAY = 1L;
    private static final Long UPDATED_DAY = 2L;

    private static final Double DEFAULT_SPLIT_RATIO = 1D;
    private static final Double UPDATED_SPLIT_RATIO = 2D;

    @Inject
    private StockSplitRepository stockSplitRepository;

    @Inject
    private StockSplitMapper stockSplitMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockSplitMockMvc;

    private StockSplit stockSplit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockSplitResource stockSplitResource = new StockSplitResource();
        ReflectionTestUtils.setField(stockSplitResource, "stockSplitRepository", stockSplitRepository);
        ReflectionTestUtils.setField(stockSplitResource, "stockSplitMapper", stockSplitMapper);
        this.restStockSplitMockMvc = MockMvcBuilders.standaloneSetup(stockSplitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockSplit = new StockSplit();
        stockSplit.setDay(DEFAULT_DAY);
        stockSplit.setSplitRatio(DEFAULT_SPLIT_RATIO);
    }

    @Test
    @Transactional
    public void createStockSplit() throws Exception {
        int databaseSizeBeforeCreate = stockSplitRepository.findAll().size();

        // Create the StockSplit
        StockSplitDTO stockSplitDTO = stockSplitMapper.stockSplitToStockSplitDTO(stockSplit);

        restStockSplitMockMvc.perform(post("/api/stockSplits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockSplitDTO)))
                .andExpect(status().isCreated());

        // Validate the StockSplit in the database
        List<StockSplit> stockSplits = stockSplitRepository.findAll();
        assertThat(stockSplits).hasSize(databaseSizeBeforeCreate + 1);
        StockSplit testStockSplit = stockSplits.get(stockSplits.size() - 1);
        assertThat(testStockSplit.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testStockSplit.getSplitRatio()).isEqualTo(DEFAULT_SPLIT_RATIO);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockSplitRepository.findAll().size();
        // set the field null
        stockSplit.setDay(null);

        // Create the StockSplit, which fails.
        StockSplitDTO stockSplitDTO = stockSplitMapper.stockSplitToStockSplitDTO(stockSplit);

        restStockSplitMockMvc.perform(post("/api/stockSplits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockSplitDTO)))
                .andExpect(status().isBadRequest());

        List<StockSplit> stockSplits = stockSplitRepository.findAll();
        assertThat(stockSplits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSplitRatioIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockSplitRepository.findAll().size();
        // set the field null
        stockSplit.setSplitRatio(null);

        // Create the StockSplit, which fails.
        StockSplitDTO stockSplitDTO = stockSplitMapper.stockSplitToStockSplitDTO(stockSplit);

        restStockSplitMockMvc.perform(post("/api/stockSplits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockSplitDTO)))
                .andExpect(status().isBadRequest());

        List<StockSplit> stockSplits = stockSplitRepository.findAll();
        assertThat(stockSplits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockSplits() throws Exception {
        // Initialize the database
        stockSplitRepository.saveAndFlush(stockSplit);

        // Get all the stockSplits
        restStockSplitMockMvc.perform(get("/api/stockSplits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockSplit.getId().intValue())))
                .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.intValue())))
                .andExpect(jsonPath("$.[*].splitRatio").value(hasItem(DEFAULT_SPLIT_RATIO.doubleValue())));
    }

    @Test
    @Transactional
    public void getStockSplit() throws Exception {
        // Initialize the database
        stockSplitRepository.saveAndFlush(stockSplit);

        // Get the stockSplit
        restStockSplitMockMvc.perform(get("/api/stockSplits/{id}", stockSplit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockSplit.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.intValue()))
            .andExpect(jsonPath("$.splitRatio").value(DEFAULT_SPLIT_RATIO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockSplit() throws Exception {
        // Get the stockSplit
        restStockSplitMockMvc.perform(get("/api/stockSplits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockSplit() throws Exception {
        // Initialize the database
        stockSplitRepository.saveAndFlush(stockSplit);

		int databaseSizeBeforeUpdate = stockSplitRepository.findAll().size();

        // Update the stockSplit
        stockSplit.setDay(UPDATED_DAY);
        stockSplit.setSplitRatio(UPDATED_SPLIT_RATIO);
        
        StockSplitDTO stockSplitDTO = stockSplitMapper.stockSplitToStockSplitDTO(stockSplit);

        restStockSplitMockMvc.perform(put("/api/stockSplits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockSplitDTO)))
                .andExpect(status().isOk());

        // Validate the StockSplit in the database
        List<StockSplit> stockSplits = stockSplitRepository.findAll();
        assertThat(stockSplits).hasSize(databaseSizeBeforeUpdate);
        StockSplit testStockSplit = stockSplits.get(stockSplits.size() - 1);
        assertThat(testStockSplit.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testStockSplit.getSplitRatio()).isEqualTo(UPDATED_SPLIT_RATIO);
    }

    @Test
    @Transactional
    public void deleteStockSplit() throws Exception {
        // Initialize the database
        stockSplitRepository.saveAndFlush(stockSplit);

		int databaseSizeBeforeDelete = stockSplitRepository.findAll().size();

        // Get the stockSplit
        restStockSplitMockMvc.perform(delete("/api/stockSplits/{id}", stockSplit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StockSplit> stockSplits = stockSplitRepository.findAll();
        assertThat(stockSplits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
