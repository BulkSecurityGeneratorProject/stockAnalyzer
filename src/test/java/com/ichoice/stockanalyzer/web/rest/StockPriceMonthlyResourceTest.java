package com.ichoice.stockanalyzer.web.rest;

import com.ichoice.stockanalyzer.Application;
import com.ichoice.stockanalyzer.domain.StockPriceMonthly;
import com.ichoice.stockanalyzer.repository.StockPriceMonthlyRepository;
import com.ichoice.stockanalyzer.web.rest.dto.StockPriceMonthlyDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockPriceMonthlyMapper;

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
 * Test class for the StockPriceMonthlyResource REST controller.
 *
 * @see StockPriceMonthlyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StockPriceMonthlyResourceTest {


    private static final Long DEFAULT_DAY = 1L;
    private static final Long UPDATED_DAY = 2L;

    private static final Integer DEFAULT_MONTH = 1;
    private static final Integer UPDATED_MONTH = 2;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Float DEFAULT_OPEN = 1F;
    private static final Float UPDATED_OPEN = 2F;

    private static final Float DEFAULT_CLOSE = 1F;
    private static final Float UPDATED_CLOSE = 2F;

    private static final Float DEFAULT_HIGH = 1F;
    private static final Float UPDATED_HIGH = 2F;

    private static final Float DEFAULT_LOW = 1F;
    private static final Float UPDATED_LOW = 2F;

    private static final Long DEFAULT_TOTAL_VOLUME = 1L;
    private static final Long UPDATED_TOTAL_VOLUME = 2L;

    private static final Float DEFAULT_ADJUSTED_CLOSE = 1F;
    private static final Float UPDATED_ADJUSTED_CLOSE = 2F;

    private static final Float DEFAULT_PRICE_CHANGE = 1F;
    private static final Float UPDATED_PRICE_CHANGE = 2F;

    @Inject
    private StockPriceMonthlyRepository stockPriceMonthlyRepository;

    @Inject
    private StockPriceMonthlyMapper stockPriceMonthlyMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockPriceMonthlyMockMvc;

    private StockPriceMonthly stockPriceMonthly;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockPriceMonthlyResource stockPriceMonthlyResource = new StockPriceMonthlyResource();
        ReflectionTestUtils.setField(stockPriceMonthlyResource, "stockPriceMonthlyRepository", stockPriceMonthlyRepository);
        ReflectionTestUtils.setField(stockPriceMonthlyResource, "stockPriceMonthlyMapper", stockPriceMonthlyMapper);
        this.restStockPriceMonthlyMockMvc = MockMvcBuilders.standaloneSetup(stockPriceMonthlyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockPriceMonthly = new StockPriceMonthly();
        stockPriceMonthly.setDay(DEFAULT_DAY);
        stockPriceMonthly.setMonth(DEFAULT_MONTH);
        stockPriceMonthly.setYear(DEFAULT_YEAR);
        stockPriceMonthly.setOpen(DEFAULT_OPEN);
        stockPriceMonthly.setClose(DEFAULT_CLOSE);
        stockPriceMonthly.setHigh(DEFAULT_HIGH);
        stockPriceMonthly.setLow(DEFAULT_LOW);
        stockPriceMonthly.setTotalVolume(DEFAULT_TOTAL_VOLUME);
        stockPriceMonthly.setAdjustedClose(DEFAULT_ADJUSTED_CLOSE);
        stockPriceMonthly.setPriceChange(DEFAULT_PRICE_CHANGE);
    }

    @Test
    @Transactional
    public void createStockPriceMonthly() throws Exception {
        int databaseSizeBeforeCreate = stockPriceMonthlyRepository.findAll().size();

        // Create the StockPriceMonthly
        StockPriceMonthlyDTO stockPriceMonthlyDTO = stockPriceMonthlyMapper.stockPriceMonthlyToStockPriceMonthlyDTO(stockPriceMonthly);

        restStockPriceMonthlyMockMvc.perform(post("/api/stockPriceMonthlys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceMonthlyDTO)))
                .andExpect(status().isCreated());

        // Validate the StockPriceMonthly in the database
        List<StockPriceMonthly> stockPriceMonthlys = stockPriceMonthlyRepository.findAll();
        assertThat(stockPriceMonthlys).hasSize(databaseSizeBeforeCreate + 1);
        StockPriceMonthly testStockPriceMonthly = stockPriceMonthlys.get(stockPriceMonthlys.size() - 1);
        assertThat(testStockPriceMonthly.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testStockPriceMonthly.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testStockPriceMonthly.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testStockPriceMonthly.getOpen()).isEqualTo(DEFAULT_OPEN);
        assertThat(testStockPriceMonthly.getClose()).isEqualTo(DEFAULT_CLOSE);
        assertThat(testStockPriceMonthly.getHigh()).isEqualTo(DEFAULT_HIGH);
        assertThat(testStockPriceMonthly.getLow()).isEqualTo(DEFAULT_LOW);
        assertThat(testStockPriceMonthly.getTotalVolume()).isEqualTo(DEFAULT_TOTAL_VOLUME);
        assertThat(testStockPriceMonthly.getAdjustedClose()).isEqualTo(DEFAULT_ADJUSTED_CLOSE);
        assertThat(testStockPriceMonthly.getPriceChange()).isEqualTo(DEFAULT_PRICE_CHANGE);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockPriceMonthlyRepository.findAll().size();
        // set the field null
        stockPriceMonthly.setDay(null);

        // Create the StockPriceMonthly, which fails.
        StockPriceMonthlyDTO stockPriceMonthlyDTO = stockPriceMonthlyMapper.stockPriceMonthlyToStockPriceMonthlyDTO(stockPriceMonthly);

        restStockPriceMonthlyMockMvc.perform(post("/api/stockPriceMonthlys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceMonthlyDTO)))
                .andExpect(status().isBadRequest());

        List<StockPriceMonthly> stockPriceMonthlys = stockPriceMonthlyRepository.findAll();
        assertThat(stockPriceMonthlys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockPriceMonthlyRepository.findAll().size();
        // set the field null
        stockPriceMonthly.setMonth(null);

        // Create the StockPriceMonthly, which fails.
        StockPriceMonthlyDTO stockPriceMonthlyDTO = stockPriceMonthlyMapper.stockPriceMonthlyToStockPriceMonthlyDTO(stockPriceMonthly);

        restStockPriceMonthlyMockMvc.perform(post("/api/stockPriceMonthlys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceMonthlyDTO)))
                .andExpect(status().isBadRequest());

        List<StockPriceMonthly> stockPriceMonthlys = stockPriceMonthlyRepository.findAll();
        assertThat(stockPriceMonthlys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockPriceMonthlyRepository.findAll().size();
        // set the field null
        stockPriceMonthly.setYear(null);

        // Create the StockPriceMonthly, which fails.
        StockPriceMonthlyDTO stockPriceMonthlyDTO = stockPriceMonthlyMapper.stockPriceMonthlyToStockPriceMonthlyDTO(stockPriceMonthly);

        restStockPriceMonthlyMockMvc.perform(post("/api/stockPriceMonthlys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceMonthlyDTO)))
                .andExpect(status().isBadRequest());

        List<StockPriceMonthly> stockPriceMonthlys = stockPriceMonthlyRepository.findAll();
        assertThat(stockPriceMonthlys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockPriceMonthlys() throws Exception {
        // Initialize the database
        stockPriceMonthlyRepository.saveAndFlush(stockPriceMonthly);

        // Get all the stockPriceMonthlys
        restStockPriceMonthlyMockMvc.perform(get("/api/stockPriceMonthlys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockPriceMonthly.getId().intValue())))
                .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.intValue())))
                .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
                .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN.doubleValue())))
                .andExpect(jsonPath("$.[*].close").value(hasItem(DEFAULT_CLOSE.doubleValue())))
                .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.doubleValue())))
                .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.doubleValue())))
                .andExpect(jsonPath("$.[*].totalVolume").value(hasItem(DEFAULT_TOTAL_VOLUME.intValue())))
                .andExpect(jsonPath("$.[*].adjustedClose").value(hasItem(DEFAULT_ADJUSTED_CLOSE.doubleValue())))
                .andExpect(jsonPath("$.[*].priceChange").value(hasItem(DEFAULT_PRICE_CHANGE.doubleValue())));
    }

    @Test
    @Transactional
    public void getStockPriceMonthly() throws Exception {
        // Initialize the database
        stockPriceMonthlyRepository.saveAndFlush(stockPriceMonthly);

        // Get the stockPriceMonthly
        restStockPriceMonthlyMockMvc.perform(get("/api/stockPriceMonthlys/{id}", stockPriceMonthly.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockPriceMonthly.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.doubleValue()))
            .andExpect(jsonPath("$.close").value(DEFAULT_CLOSE.doubleValue()))
            .andExpect(jsonPath("$.high").value(DEFAULT_HIGH.doubleValue()))
            .andExpect(jsonPath("$.low").value(DEFAULT_LOW.doubleValue()))
            .andExpect(jsonPath("$.totalVolume").value(DEFAULT_TOTAL_VOLUME.intValue()))
            .andExpect(jsonPath("$.adjustedClose").value(DEFAULT_ADJUSTED_CLOSE.doubleValue()))
            .andExpect(jsonPath("$.priceChange").value(DEFAULT_PRICE_CHANGE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockPriceMonthly() throws Exception {
        // Get the stockPriceMonthly
        restStockPriceMonthlyMockMvc.perform(get("/api/stockPriceMonthlys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockPriceMonthly() throws Exception {
        // Initialize the database
        stockPriceMonthlyRepository.saveAndFlush(stockPriceMonthly);

		int databaseSizeBeforeUpdate = stockPriceMonthlyRepository.findAll().size();

        // Update the stockPriceMonthly
        stockPriceMonthly.setDay(UPDATED_DAY);
        stockPriceMonthly.setMonth(UPDATED_MONTH);
        stockPriceMonthly.setYear(UPDATED_YEAR);
        stockPriceMonthly.setOpen(UPDATED_OPEN);
        stockPriceMonthly.setClose(UPDATED_CLOSE);
        stockPriceMonthly.setHigh(UPDATED_HIGH);
        stockPriceMonthly.setLow(UPDATED_LOW);
        stockPriceMonthly.setTotalVolume(UPDATED_TOTAL_VOLUME);
        stockPriceMonthly.setAdjustedClose(UPDATED_ADJUSTED_CLOSE);
        stockPriceMonthly.setPriceChange(UPDATED_PRICE_CHANGE);
        
        StockPriceMonthlyDTO stockPriceMonthlyDTO = stockPriceMonthlyMapper.stockPriceMonthlyToStockPriceMonthlyDTO(stockPriceMonthly);

        restStockPriceMonthlyMockMvc.perform(put("/api/stockPriceMonthlys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceMonthlyDTO)))
                .andExpect(status().isOk());

        // Validate the StockPriceMonthly in the database
        List<StockPriceMonthly> stockPriceMonthlys = stockPriceMonthlyRepository.findAll();
        assertThat(stockPriceMonthlys).hasSize(databaseSizeBeforeUpdate);
        StockPriceMonthly testStockPriceMonthly = stockPriceMonthlys.get(stockPriceMonthlys.size() - 1);
        assertThat(testStockPriceMonthly.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testStockPriceMonthly.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testStockPriceMonthly.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testStockPriceMonthly.getOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testStockPriceMonthly.getClose()).isEqualTo(UPDATED_CLOSE);
        assertThat(testStockPriceMonthly.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testStockPriceMonthly.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testStockPriceMonthly.getTotalVolume()).isEqualTo(UPDATED_TOTAL_VOLUME);
        assertThat(testStockPriceMonthly.getAdjustedClose()).isEqualTo(UPDATED_ADJUSTED_CLOSE);
        assertThat(testStockPriceMonthly.getPriceChange()).isEqualTo(UPDATED_PRICE_CHANGE);
    }

    @Test
    @Transactional
    public void deleteStockPriceMonthly() throws Exception {
        // Initialize the database
        stockPriceMonthlyRepository.saveAndFlush(stockPriceMonthly);

		int databaseSizeBeforeDelete = stockPriceMonthlyRepository.findAll().size();

        // Get the stockPriceMonthly
        restStockPriceMonthlyMockMvc.perform(delete("/api/stockPriceMonthlys/{id}", stockPriceMonthly.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StockPriceMonthly> stockPriceMonthlys = stockPriceMonthlyRepository.findAll();
        assertThat(stockPriceMonthlys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
