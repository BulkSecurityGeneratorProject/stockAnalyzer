package com.ichoice.stockanalyzer.web.rest;

import com.ichoice.stockanalyzer.Application;
import com.ichoice.stockanalyzer.domain.StockPriceWeekly;
import com.ichoice.stockanalyzer.repository.StockPriceWeeklyRepository;
import com.ichoice.stockanalyzer.web.rest.dto.StockPriceWeeklyDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockPriceWeeklyMapper;

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
 * Test class for the StockPriceWeeklyResource REST controller.
 *
 * @see StockPriceWeeklyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StockPriceWeeklyResourceTest {


    private static final Long DEFAULT_DAY = 1L;
    private static final Long UPDATED_DAY = 2L;

    private static final Integer DEFAULT_WEEK = 1;
    private static final Integer UPDATED_WEEK = 2;

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
    private StockPriceWeeklyRepository stockPriceWeeklyRepository;

    @Inject
    private StockPriceWeeklyMapper stockPriceWeeklyMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockPriceWeeklyMockMvc;

    private StockPriceWeekly stockPriceWeekly;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockPriceWeeklyResource stockPriceWeeklyResource = new StockPriceWeeklyResource();
        ReflectionTestUtils.setField(stockPriceWeeklyResource, "stockPriceWeeklyRepository", stockPriceWeeklyRepository);
        ReflectionTestUtils.setField(stockPriceWeeklyResource, "stockPriceWeeklyMapper", stockPriceWeeklyMapper);
        this.restStockPriceWeeklyMockMvc = MockMvcBuilders.standaloneSetup(stockPriceWeeklyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockPriceWeekly = new StockPriceWeekly();
        stockPriceWeekly.setDay(DEFAULT_DAY);
        stockPriceWeekly.setWeek(DEFAULT_WEEK);
        stockPriceWeekly.setYear(DEFAULT_YEAR);
        stockPriceWeekly.setOpen(DEFAULT_OPEN);
        stockPriceWeekly.setClose(DEFAULT_CLOSE);
        stockPriceWeekly.setHigh(DEFAULT_HIGH);
        stockPriceWeekly.setLow(DEFAULT_LOW);
        stockPriceWeekly.setTotalVolume(DEFAULT_TOTAL_VOLUME);
        stockPriceWeekly.setAdjustedClose(DEFAULT_ADJUSTED_CLOSE);
        stockPriceWeekly.setPriceChange(DEFAULT_PRICE_CHANGE);
    }

    @Test
    @Transactional
    public void createStockPriceWeekly() throws Exception {
        int databaseSizeBeforeCreate = stockPriceWeeklyRepository.findAll().size();

        // Create the StockPriceWeekly
        StockPriceWeeklyDTO stockPriceWeeklyDTO = stockPriceWeeklyMapper.stockPriceWeeklyToStockPriceWeeklyDTO(stockPriceWeekly);

        restStockPriceWeeklyMockMvc.perform(post("/api/stockPriceWeeklys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceWeeklyDTO)))
                .andExpect(status().isCreated());

        // Validate the StockPriceWeekly in the database
        List<StockPriceWeekly> stockPriceWeeklys = stockPriceWeeklyRepository.findAll();
        assertThat(stockPriceWeeklys).hasSize(databaseSizeBeforeCreate + 1);
        StockPriceWeekly testStockPriceWeekly = stockPriceWeeklys.get(stockPriceWeeklys.size() - 1);
        assertThat(testStockPriceWeekly.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testStockPriceWeekly.getWeek()).isEqualTo(DEFAULT_WEEK);
        assertThat(testStockPriceWeekly.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testStockPriceWeekly.getOpen()).isEqualTo(DEFAULT_OPEN);
        assertThat(testStockPriceWeekly.getClose()).isEqualTo(DEFAULT_CLOSE);
        assertThat(testStockPriceWeekly.getHigh()).isEqualTo(DEFAULT_HIGH);
        assertThat(testStockPriceWeekly.getLow()).isEqualTo(DEFAULT_LOW);
        assertThat(testStockPriceWeekly.getTotalVolume()).isEqualTo(DEFAULT_TOTAL_VOLUME);
        assertThat(testStockPriceWeekly.getAdjustedClose()).isEqualTo(DEFAULT_ADJUSTED_CLOSE);
        assertThat(testStockPriceWeekly.getPriceChange()).isEqualTo(DEFAULT_PRICE_CHANGE);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockPriceWeeklyRepository.findAll().size();
        // set the field null
        stockPriceWeekly.setDay(null);

        // Create the StockPriceWeekly, which fails.
        StockPriceWeeklyDTO stockPriceWeeklyDTO = stockPriceWeeklyMapper.stockPriceWeeklyToStockPriceWeeklyDTO(stockPriceWeekly);

        restStockPriceWeeklyMockMvc.perform(post("/api/stockPriceWeeklys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceWeeklyDTO)))
                .andExpect(status().isBadRequest());

        List<StockPriceWeekly> stockPriceWeeklys = stockPriceWeeklyRepository.findAll();
        assertThat(stockPriceWeeklys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeekIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockPriceWeeklyRepository.findAll().size();
        // set the field null
        stockPriceWeekly.setWeek(null);

        // Create the StockPriceWeekly, which fails.
        StockPriceWeeklyDTO stockPriceWeeklyDTO = stockPriceWeeklyMapper.stockPriceWeeklyToStockPriceWeeklyDTO(stockPriceWeekly);

        restStockPriceWeeklyMockMvc.perform(post("/api/stockPriceWeeklys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceWeeklyDTO)))
                .andExpect(status().isBadRequest());

        List<StockPriceWeekly> stockPriceWeeklys = stockPriceWeeklyRepository.findAll();
        assertThat(stockPriceWeeklys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockPriceWeeklyRepository.findAll().size();
        // set the field null
        stockPriceWeekly.setYear(null);

        // Create the StockPriceWeekly, which fails.
        StockPriceWeeklyDTO stockPriceWeeklyDTO = stockPriceWeeklyMapper.stockPriceWeeklyToStockPriceWeeklyDTO(stockPriceWeekly);

        restStockPriceWeeklyMockMvc.perform(post("/api/stockPriceWeeklys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceWeeklyDTO)))
                .andExpect(status().isBadRequest());

        List<StockPriceWeekly> stockPriceWeeklys = stockPriceWeeklyRepository.findAll();
        assertThat(stockPriceWeeklys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockPriceWeeklys() throws Exception {
        // Initialize the database
        stockPriceWeeklyRepository.saveAndFlush(stockPriceWeekly);

        // Get all the stockPriceWeeklys
        restStockPriceWeeklyMockMvc.perform(get("/api/stockPriceWeeklys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockPriceWeekly.getId().intValue())))
                .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.intValue())))
                .andExpect(jsonPath("$.[*].week").value(hasItem(DEFAULT_WEEK)))
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
    public void getStockPriceWeekly() throws Exception {
        // Initialize the database
        stockPriceWeeklyRepository.saveAndFlush(stockPriceWeekly);

        // Get the stockPriceWeekly
        restStockPriceWeeklyMockMvc.perform(get("/api/stockPriceWeeklys/{id}", stockPriceWeekly.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockPriceWeekly.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.intValue()))
            .andExpect(jsonPath("$.week").value(DEFAULT_WEEK))
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
    public void getNonExistingStockPriceWeekly() throws Exception {
        // Get the stockPriceWeekly
        restStockPriceWeeklyMockMvc.perform(get("/api/stockPriceWeeklys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockPriceWeekly() throws Exception {
        // Initialize the database
        stockPriceWeeklyRepository.saveAndFlush(stockPriceWeekly);

		int databaseSizeBeforeUpdate = stockPriceWeeklyRepository.findAll().size();

        // Update the stockPriceWeekly
        stockPriceWeekly.setDay(UPDATED_DAY);
        stockPriceWeekly.setWeek(UPDATED_WEEK);
        stockPriceWeekly.setYear(UPDATED_YEAR);
        stockPriceWeekly.setOpen(UPDATED_OPEN);
        stockPriceWeekly.setClose(UPDATED_CLOSE);
        stockPriceWeekly.setHigh(UPDATED_HIGH);
        stockPriceWeekly.setLow(UPDATED_LOW);
        stockPriceWeekly.setTotalVolume(UPDATED_TOTAL_VOLUME);
        stockPriceWeekly.setAdjustedClose(UPDATED_ADJUSTED_CLOSE);
        stockPriceWeekly.setPriceChange(UPDATED_PRICE_CHANGE);
        
        StockPriceWeeklyDTO stockPriceWeeklyDTO = stockPriceWeeklyMapper.stockPriceWeeklyToStockPriceWeeklyDTO(stockPriceWeekly);

        restStockPriceWeeklyMockMvc.perform(put("/api/stockPriceWeeklys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceWeeklyDTO)))
                .andExpect(status().isOk());

        // Validate the StockPriceWeekly in the database
        List<StockPriceWeekly> stockPriceWeeklys = stockPriceWeeklyRepository.findAll();
        assertThat(stockPriceWeeklys).hasSize(databaseSizeBeforeUpdate);
        StockPriceWeekly testStockPriceWeekly = stockPriceWeeklys.get(stockPriceWeeklys.size() - 1);
        assertThat(testStockPriceWeekly.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testStockPriceWeekly.getWeek()).isEqualTo(UPDATED_WEEK);
        assertThat(testStockPriceWeekly.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testStockPriceWeekly.getOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testStockPriceWeekly.getClose()).isEqualTo(UPDATED_CLOSE);
        assertThat(testStockPriceWeekly.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testStockPriceWeekly.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testStockPriceWeekly.getTotalVolume()).isEqualTo(UPDATED_TOTAL_VOLUME);
        assertThat(testStockPriceWeekly.getAdjustedClose()).isEqualTo(UPDATED_ADJUSTED_CLOSE);
        assertThat(testStockPriceWeekly.getPriceChange()).isEqualTo(UPDATED_PRICE_CHANGE);
    }

    @Test
    @Transactional
    public void deleteStockPriceWeekly() throws Exception {
        // Initialize the database
        stockPriceWeeklyRepository.saveAndFlush(stockPriceWeekly);

		int databaseSizeBeforeDelete = stockPriceWeeklyRepository.findAll().size();

        // Get the stockPriceWeekly
        restStockPriceWeeklyMockMvc.perform(delete("/api/stockPriceWeeklys/{id}", stockPriceWeekly.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StockPriceWeekly> stockPriceWeeklys = stockPriceWeeklyRepository.findAll();
        assertThat(stockPriceWeeklys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
