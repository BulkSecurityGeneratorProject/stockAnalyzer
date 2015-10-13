package com.ichoice.stockanalyzer.web.rest;

import com.ichoice.stockanalyzer.Application;
import com.ichoice.stockanalyzer.domain.StockDividend;
import com.ichoice.stockanalyzer.repository.StockDividendRepository;
import com.ichoice.stockanalyzer.web.rest.dto.StockDividendDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockDividendMapper;

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
 * Test class for the StockDividendResource REST controller.
 *
 * @see StockDividendResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StockDividendResourceTest {


    private static final Long DEFAULT_DAY = 1L;
    private static final Long UPDATED_DAY = 2L;

    private static final Float DEFAULT_AMOUNT = 0F;
    private static final Float UPDATED_AMOUNT = 1F;

    @Inject
    private StockDividendRepository stockDividendRepository;

    @Inject
    private StockDividendMapper stockDividendMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockDividendMockMvc;

    private StockDividend stockDividend;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockDividendResource stockDividendResource = new StockDividendResource();
        ReflectionTestUtils.setField(stockDividendResource, "stockDividendRepository", stockDividendRepository);
        ReflectionTestUtils.setField(stockDividendResource, "stockDividendMapper", stockDividendMapper);
        this.restStockDividendMockMvc = MockMvcBuilders.standaloneSetup(stockDividendResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockDividend = new StockDividend();
        stockDividend.setDay(DEFAULT_DAY);
        stockDividend.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createStockDividend() throws Exception {
        int databaseSizeBeforeCreate = stockDividendRepository.findAll().size();

        // Create the StockDividend
        StockDividendDTO stockDividendDTO = stockDividendMapper.stockDividendToStockDividendDTO(stockDividend);

        restStockDividendMockMvc.perform(post("/api/stockDividends")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockDividendDTO)))
                .andExpect(status().isCreated());

        // Validate the StockDividend in the database
        List<StockDividend> stockDividends = stockDividendRepository.findAll();
        assertThat(stockDividends).hasSize(databaseSizeBeforeCreate + 1);
        StockDividend testStockDividend = stockDividends.get(stockDividends.size() - 1);
        assertThat(testStockDividend.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testStockDividend.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockDividendRepository.findAll().size();
        // set the field null
        stockDividend.setDay(null);

        // Create the StockDividend, which fails.
        StockDividendDTO stockDividendDTO = stockDividendMapper.stockDividendToStockDividendDTO(stockDividend);

        restStockDividendMockMvc.perform(post("/api/stockDividends")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockDividendDTO)))
                .andExpect(status().isBadRequest());

        List<StockDividend> stockDividends = stockDividendRepository.findAll();
        assertThat(stockDividends).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockDividendRepository.findAll().size();
        // set the field null
        stockDividend.setAmount(null);

        // Create the StockDividend, which fails.
        StockDividendDTO stockDividendDTO = stockDividendMapper.stockDividendToStockDividendDTO(stockDividend);

        restStockDividendMockMvc.perform(post("/api/stockDividends")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockDividendDTO)))
                .andExpect(status().isBadRequest());

        List<StockDividend> stockDividends = stockDividendRepository.findAll();
        assertThat(stockDividends).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockDividends() throws Exception {
        // Initialize the database
        stockDividendRepository.saveAndFlush(stockDividend);

        // Get all the stockDividends
        restStockDividendMockMvc.perform(get("/api/stockDividends"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockDividend.getId().intValue())))
                .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getStockDividend() throws Exception {
        // Initialize the database
        stockDividendRepository.saveAndFlush(stockDividend);

        // Get the stockDividend
        restStockDividendMockMvc.perform(get("/api/stockDividends/{id}", stockDividend.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockDividend.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockDividend() throws Exception {
        // Get the stockDividend
        restStockDividendMockMvc.perform(get("/api/stockDividends/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockDividend() throws Exception {
        // Initialize the database
        stockDividendRepository.saveAndFlush(stockDividend);

		int databaseSizeBeforeUpdate = stockDividendRepository.findAll().size();

        // Update the stockDividend
        stockDividend.setDay(UPDATED_DAY);
        stockDividend.setAmount(UPDATED_AMOUNT);
        
        StockDividendDTO stockDividendDTO = stockDividendMapper.stockDividendToStockDividendDTO(stockDividend);

        restStockDividendMockMvc.perform(put("/api/stockDividends")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockDividendDTO)))
                .andExpect(status().isOk());

        // Validate the StockDividend in the database
        List<StockDividend> stockDividends = stockDividendRepository.findAll();
        assertThat(stockDividends).hasSize(databaseSizeBeforeUpdate);
        StockDividend testStockDividend = stockDividends.get(stockDividends.size() - 1);
        assertThat(testStockDividend.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testStockDividend.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteStockDividend() throws Exception {
        // Initialize the database
        stockDividendRepository.saveAndFlush(stockDividend);

		int databaseSizeBeforeDelete = stockDividendRepository.findAll().size();

        // Get the stockDividend
        restStockDividendMockMvc.perform(delete("/api/stockDividends/{id}", stockDividend.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StockDividend> stockDividends = stockDividendRepository.findAll();
        assertThat(stockDividends).hasSize(databaseSizeBeforeDelete - 1);
    }
}
