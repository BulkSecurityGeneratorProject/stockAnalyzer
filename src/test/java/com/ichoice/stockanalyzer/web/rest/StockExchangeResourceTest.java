package com.ichoice.stockanalyzer.web.rest;

import com.ichoice.stockanalyzer.Application;
import com.ichoice.stockanalyzer.domain.StockExchange;
import com.ichoice.stockanalyzer.repository.StockExchangeRepository;
import com.ichoice.stockanalyzer.web.rest.dto.StockExchangeDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockExchangeMapper;

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
 * Test class for the StockExchangeResource REST controller.
 *
 * @see StockExchangeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StockExchangeResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private StockExchangeRepository stockExchangeRepository;

    @Inject
    private StockExchangeMapper stockExchangeMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockExchangeMockMvc;

    private StockExchange stockExchange;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockExchangeResource stockExchangeResource = new StockExchangeResource();
        ReflectionTestUtils.setField(stockExchangeResource, "stockExchangeRepository", stockExchangeRepository);
        ReflectionTestUtils.setField(stockExchangeResource, "stockExchangeMapper", stockExchangeMapper);
        this.restStockExchangeMockMvc = MockMvcBuilders.standaloneSetup(stockExchangeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockExchange = new StockExchange();
        stockExchange.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createStockExchange() throws Exception {
        int databaseSizeBeforeCreate = stockExchangeRepository.findAll().size();

        // Create the StockExchange
        StockExchangeDTO stockExchangeDTO = stockExchangeMapper.stockExchangeToStockExchangeDTO(stockExchange);

        restStockExchangeMockMvc.perform(post("/api/stockExchanges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockExchangeDTO)))
                .andExpect(status().isCreated());

        // Validate the StockExchange in the database
        List<StockExchange> stockExchanges = stockExchangeRepository.findAll();
        assertThat(stockExchanges).hasSize(databaseSizeBeforeCreate + 1);
        StockExchange testStockExchange = stockExchanges.get(stockExchanges.size() - 1);
        assertThat(testStockExchange.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllStockExchanges() throws Exception {
        // Initialize the database
        stockExchangeRepository.saveAndFlush(stockExchange);

        // Get all the stockExchanges
        restStockExchangeMockMvc.perform(get("/api/stockExchanges"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockExchange.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getStockExchange() throws Exception {
        // Initialize the database
        stockExchangeRepository.saveAndFlush(stockExchange);

        // Get the stockExchange
        restStockExchangeMockMvc.perform(get("/api/stockExchanges/{id}", stockExchange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockExchange.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockExchange() throws Exception {
        // Get the stockExchange
        restStockExchangeMockMvc.perform(get("/api/stockExchanges/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockExchange() throws Exception {
        // Initialize the database
        stockExchangeRepository.saveAndFlush(stockExchange);

		int databaseSizeBeforeUpdate = stockExchangeRepository.findAll().size();

        // Update the stockExchange
        stockExchange.setName(UPDATED_NAME);
        
        StockExchangeDTO stockExchangeDTO = stockExchangeMapper.stockExchangeToStockExchangeDTO(stockExchange);

        restStockExchangeMockMvc.perform(put("/api/stockExchanges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockExchangeDTO)))
                .andExpect(status().isOk());

        // Validate the StockExchange in the database
        List<StockExchange> stockExchanges = stockExchangeRepository.findAll();
        assertThat(stockExchanges).hasSize(databaseSizeBeforeUpdate);
        StockExchange testStockExchange = stockExchanges.get(stockExchanges.size() - 1);
        assertThat(testStockExchange.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteStockExchange() throws Exception {
        // Initialize the database
        stockExchangeRepository.saveAndFlush(stockExchange);

		int databaseSizeBeforeDelete = stockExchangeRepository.findAll().size();

        // Get the stockExchange
        restStockExchangeMockMvc.perform(delete("/api/stockExchanges/{id}", stockExchange.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StockExchange> stockExchanges = stockExchangeRepository.findAll();
        assertThat(stockExchanges).hasSize(databaseSizeBeforeDelete - 1);
    }
}
