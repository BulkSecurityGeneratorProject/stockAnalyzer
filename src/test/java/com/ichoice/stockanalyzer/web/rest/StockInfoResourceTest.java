package com.ichoice.stockanalyzer.web.rest;

import com.ichoice.stockanalyzer.Application;
import com.ichoice.stockanalyzer.domain.StockInfo;
import com.ichoice.stockanalyzer.repository.StockInfoRepository;
import com.ichoice.stockanalyzer.web.rest.dto.StockInfoDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockInfoMapper;

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
 * Test class for the StockInfoResource REST controller.
 *
 * @see StockInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StockInfoResourceTest {

    private static final String DEFAULT_TICKER = "SAMPLE_TEXT";
    private static final String UPDATED_TICKER = "UPDATED_TEXT";
    private static final String DEFAULT_COMPANY_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_COMPANY_NAME = "UPDATED_TEXT";

    @Inject
    private StockInfoRepository stockInfoRepository;

    @Inject
    private StockInfoMapper stockInfoMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockInfoMockMvc;

    private StockInfo stockInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockInfoResource stockInfoResource = new StockInfoResource();
        ReflectionTestUtils.setField(stockInfoResource, "stockInfoRepository", stockInfoRepository);
        ReflectionTestUtils.setField(stockInfoResource, "stockInfoMapper", stockInfoMapper);
        this.restStockInfoMockMvc = MockMvcBuilders.standaloneSetup(stockInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockInfo = new StockInfo();
        stockInfo.setTicker(DEFAULT_TICKER);
        stockInfo.setCompanyName(DEFAULT_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void createStockInfo() throws Exception {
        int databaseSizeBeforeCreate = stockInfoRepository.findAll().size();

        // Create the StockInfo
        StockInfoDTO stockInfoDTO = stockInfoMapper.stockInfoToStockInfoDTO(stockInfo);

        restStockInfoMockMvc.perform(post("/api/stockInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the StockInfo in the database
        List<StockInfo> stockInfos = stockInfoRepository.findAll();
        assertThat(stockInfos).hasSize(databaseSizeBeforeCreate + 1);
        StockInfo testStockInfo = stockInfos.get(stockInfos.size() - 1);
        assertThat(testStockInfo.getTicker()).isEqualTo(DEFAULT_TICKER);
        assertThat(testStockInfo.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllStockInfos() throws Exception {
        // Initialize the database
        stockInfoRepository.saveAndFlush(stockInfo);

        // Get all the stockInfos
        restStockInfoMockMvc.perform(get("/api/stockInfos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].ticker").value(hasItem(DEFAULT_TICKER.toString())))
                .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getStockInfo() throws Exception {
        // Initialize the database
        stockInfoRepository.saveAndFlush(stockInfo);

        // Get the stockInfo
        restStockInfoMockMvc.perform(get("/api/stockInfos/{id}", stockInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockInfo.getId().intValue()))
            .andExpect(jsonPath("$.ticker").value(DEFAULT_TICKER.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockInfo() throws Exception {
        // Get the stockInfo
        restStockInfoMockMvc.perform(get("/api/stockInfos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockInfo() throws Exception {
        // Initialize the database
        stockInfoRepository.saveAndFlush(stockInfo);

		int databaseSizeBeforeUpdate = stockInfoRepository.findAll().size();

        // Update the stockInfo
        stockInfo.setTicker(UPDATED_TICKER);
        stockInfo.setCompanyName(UPDATED_COMPANY_NAME);
        
        StockInfoDTO stockInfoDTO = stockInfoMapper.stockInfoToStockInfoDTO(stockInfo);

        restStockInfoMockMvc.perform(put("/api/stockInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockInfoDTO)))
                .andExpect(status().isOk());

        // Validate the StockInfo in the database
        List<StockInfo> stockInfos = stockInfoRepository.findAll();
        assertThat(stockInfos).hasSize(databaseSizeBeforeUpdate);
        StockInfo testStockInfo = stockInfos.get(stockInfos.size() - 1);
        assertThat(testStockInfo.getTicker()).isEqualTo(UPDATED_TICKER);
        assertThat(testStockInfo.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void deleteStockInfo() throws Exception {
        // Initialize the database
        stockInfoRepository.saveAndFlush(stockInfo);

		int databaseSizeBeforeDelete = stockInfoRepository.findAll().size();

        // Get the stockInfo
        restStockInfoMockMvc.perform(delete("/api/stockInfos/{id}", stockInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StockInfo> stockInfos = stockInfoRepository.findAll();
        assertThat(stockInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
