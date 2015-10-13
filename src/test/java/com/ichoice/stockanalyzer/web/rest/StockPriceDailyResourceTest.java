package com.ichoice.stockanalyzer.web.rest;

import com.ichoice.stockanalyzer.Application;
import com.ichoice.stockanalyzer.domain.StockPriceDaily;
import com.ichoice.stockanalyzer.repository.StockPriceDailyRepository;
import com.ichoice.stockanalyzer.web.rest.dto.StockPriceDailyDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockPriceDailyMapper;

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
 * Test class for the StockPriceDailyResource REST controller.
 *
 * @see StockPriceDailyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StockPriceDailyResourceTest {


    private static final Long DEFAULT_DAY = 1L;
    private static final Long UPDATED_DAY = 2L;

    private static final Float DEFAULT_OPEN = 1F;
    private static final Float UPDATED_OPEN = 2F;

    private static final Float DEFAULT_CLOSE = 1F;
    private static final Float UPDATED_CLOSE = 2F;

    private static final Float DEFAULT_HIGH = 1F;
    private static final Float UPDATED_HIGH = 2F;

    private static final Float DEFAULT_LOW = 1F;
    private static final Float UPDATED_LOW = 2F;

    private static final Long DEFAULT_VOLUME = 1L;
    private static final Long UPDATED_VOLUME = 2L;

    private static final Float DEFAULT_ADJUSTED_CLOSE = 1F;
    private static final Float UPDATED_ADJUSTED_CLOSE = 2F;

    @Inject
    private StockPriceDailyRepository stockPriceDailyRepository;

    @Inject
    private StockPriceDailyMapper stockPriceDailyMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockPriceDailyMockMvc;

    private StockPriceDaily stockPriceDaily;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockPriceDailyResource stockPriceDailyResource = new StockPriceDailyResource();
        ReflectionTestUtils.setField(stockPriceDailyResource, "stockPriceDailyRepository", stockPriceDailyRepository);
        ReflectionTestUtils.setField(stockPriceDailyResource, "stockPriceDailyMapper", stockPriceDailyMapper);
        this.restStockPriceDailyMockMvc = MockMvcBuilders.standaloneSetup(stockPriceDailyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockPriceDaily = new StockPriceDaily();
        stockPriceDaily.setDay(DEFAULT_DAY);
        stockPriceDaily.setOpen(DEFAULT_OPEN);
        stockPriceDaily.setClose(DEFAULT_CLOSE);
        stockPriceDaily.setHigh(DEFAULT_HIGH);
        stockPriceDaily.setLow(DEFAULT_LOW);
        stockPriceDaily.setVolume(DEFAULT_VOLUME);
        stockPriceDaily.setAdjustedClose(DEFAULT_ADJUSTED_CLOSE);
    }

    @Test
    @Transactional
    public void createStockPriceDaily() throws Exception {
        int databaseSizeBeforeCreate = stockPriceDailyRepository.findAll().size();

        // Create the StockPriceDaily
        StockPriceDailyDTO stockPriceDailyDTO = stockPriceDailyMapper.stockPriceDailyToStockPriceDailyDTO(stockPriceDaily);

        restStockPriceDailyMockMvc.perform(post("/api/stockPriceDailys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceDailyDTO)))
                .andExpect(status().isCreated());

        // Validate the StockPriceDaily in the database
        List<StockPriceDaily> stockPriceDailys = stockPriceDailyRepository.findAll();
        assertThat(stockPriceDailys).hasSize(databaseSizeBeforeCreate + 1);
        StockPriceDaily testStockPriceDaily = stockPriceDailys.get(stockPriceDailys.size() - 1);
        assertThat(testStockPriceDaily.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testStockPriceDaily.getOpen()).isEqualTo(DEFAULT_OPEN);
        assertThat(testStockPriceDaily.getClose()).isEqualTo(DEFAULT_CLOSE);
        assertThat(testStockPriceDaily.getHigh()).isEqualTo(DEFAULT_HIGH);
        assertThat(testStockPriceDaily.getLow()).isEqualTo(DEFAULT_LOW);
        assertThat(testStockPriceDaily.getVolume()).isEqualTo(DEFAULT_VOLUME);
        assertThat(testStockPriceDaily.getAdjustedClose()).isEqualTo(DEFAULT_ADJUSTED_CLOSE);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockPriceDailyRepository.findAll().size();
        // set the field null
        stockPriceDaily.setDay(null);

        // Create the StockPriceDaily, which fails.
        StockPriceDailyDTO stockPriceDailyDTO = stockPriceDailyMapper.stockPriceDailyToStockPriceDailyDTO(stockPriceDaily);

        restStockPriceDailyMockMvc.perform(post("/api/stockPriceDailys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceDailyDTO)))
                .andExpect(status().isBadRequest());

        List<StockPriceDaily> stockPriceDailys = stockPriceDailyRepository.findAll();
        assertThat(stockPriceDailys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockPriceDailys() throws Exception {
        // Initialize the database
        stockPriceDailyRepository.saveAndFlush(stockPriceDaily);

        // Get all the stockPriceDailys
        restStockPriceDailyMockMvc.perform(get("/api/stockPriceDailys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockPriceDaily.getId().intValue())))
                .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.intValue())))
                .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN.doubleValue())))
                .andExpect(jsonPath("$.[*].close").value(hasItem(DEFAULT_CLOSE.doubleValue())))
                .andExpect(jsonPath("$.[*].high").value(hasItem(DEFAULT_HIGH.doubleValue())))
                .andExpect(jsonPath("$.[*].low").value(hasItem(DEFAULT_LOW.doubleValue())))
                .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME.intValue())))
                .andExpect(jsonPath("$.[*].adjustedClose").value(hasItem(DEFAULT_ADJUSTED_CLOSE.doubleValue())));
    }

    @Test
    @Transactional
    public void getStockPriceDaily() throws Exception {
        // Initialize the database
        stockPriceDailyRepository.saveAndFlush(stockPriceDaily);

        // Get the stockPriceDaily
        restStockPriceDailyMockMvc.perform(get("/api/stockPriceDailys/{id}", stockPriceDaily.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockPriceDaily.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.intValue()))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.doubleValue()))
            .andExpect(jsonPath("$.close").value(DEFAULT_CLOSE.doubleValue()))
            .andExpect(jsonPath("$.high").value(DEFAULT_HIGH.doubleValue()))
            .andExpect(jsonPath("$.low").value(DEFAULT_LOW.doubleValue()))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME.intValue()))
            .andExpect(jsonPath("$.adjustedClose").value(DEFAULT_ADJUSTED_CLOSE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockPriceDaily() throws Exception {
        // Get the stockPriceDaily
        restStockPriceDailyMockMvc.perform(get("/api/stockPriceDailys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockPriceDaily() throws Exception {
        // Initialize the database
        stockPriceDailyRepository.saveAndFlush(stockPriceDaily);

		int databaseSizeBeforeUpdate = stockPriceDailyRepository.findAll().size();

        // Update the stockPriceDaily
        stockPriceDaily.setDay(UPDATED_DAY);
        stockPriceDaily.setOpen(UPDATED_OPEN);
        stockPriceDaily.setClose(UPDATED_CLOSE);
        stockPriceDaily.setHigh(UPDATED_HIGH);
        stockPriceDaily.setLow(UPDATED_LOW);
        stockPriceDaily.setVolume(UPDATED_VOLUME);
        stockPriceDaily.setAdjustedClose(UPDATED_ADJUSTED_CLOSE);
        
        StockPriceDailyDTO stockPriceDailyDTO = stockPriceDailyMapper.stockPriceDailyToStockPriceDailyDTO(stockPriceDaily);

        restStockPriceDailyMockMvc.perform(put("/api/stockPriceDailys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockPriceDailyDTO)))
                .andExpect(status().isOk());

        // Validate the StockPriceDaily in the database
        List<StockPriceDaily> stockPriceDailys = stockPriceDailyRepository.findAll();
        assertThat(stockPriceDailys).hasSize(databaseSizeBeforeUpdate);
        StockPriceDaily testStockPriceDaily = stockPriceDailys.get(stockPriceDailys.size() - 1);
        assertThat(testStockPriceDaily.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testStockPriceDaily.getOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testStockPriceDaily.getClose()).isEqualTo(UPDATED_CLOSE);
        assertThat(testStockPriceDaily.getHigh()).isEqualTo(UPDATED_HIGH);
        assertThat(testStockPriceDaily.getLow()).isEqualTo(UPDATED_LOW);
        assertThat(testStockPriceDaily.getVolume()).isEqualTo(UPDATED_VOLUME);
        assertThat(testStockPriceDaily.getAdjustedClose()).isEqualTo(UPDATED_ADJUSTED_CLOSE);
    }

    @Test
    @Transactional
    public void deleteStockPriceDaily() throws Exception {
        // Initialize the database
        stockPriceDailyRepository.saveAndFlush(stockPriceDaily);

		int databaseSizeBeforeDelete = stockPriceDailyRepository.findAll().size();

        // Get the stockPriceDaily
        restStockPriceDailyMockMvc.perform(delete("/api/stockPriceDailys/{id}", stockPriceDaily.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StockPriceDaily> stockPriceDailys = stockPriceDailyRepository.findAll();
        assertThat(stockPriceDailys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
