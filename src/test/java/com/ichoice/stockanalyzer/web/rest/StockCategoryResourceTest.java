package com.ichoice.stockanalyzer.web.rest;

import com.ichoice.stockanalyzer.Application;
import com.ichoice.stockanalyzer.domain.StockCategory;
import com.ichoice.stockanalyzer.repository.StockCategoryRepository;
import com.ichoice.stockanalyzer.web.rest.dto.StockCategoryDTO;
import com.ichoice.stockanalyzer.web.rest.mapper.StockCategoryMapper;

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
 * Test class for the StockCategoryResource REST controller.
 *
 * @see StockCategoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StockCategoryResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private StockCategoryRepository stockCategoryRepository;

    @Inject
    private StockCategoryMapper stockCategoryMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockCategoryMockMvc;

    private StockCategory stockCategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockCategoryResource stockCategoryResource = new StockCategoryResource();
        ReflectionTestUtils.setField(stockCategoryResource, "stockCategoryRepository", stockCategoryRepository);
        ReflectionTestUtils.setField(stockCategoryResource, "stockCategoryMapper", stockCategoryMapper);
        this.restStockCategoryMockMvc = MockMvcBuilders.standaloneSetup(stockCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockCategory = new StockCategory();
        stockCategory.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createStockCategory() throws Exception {
        int databaseSizeBeforeCreate = stockCategoryRepository.findAll().size();

        // Create the StockCategory
        StockCategoryDTO stockCategoryDTO = stockCategoryMapper.stockCategoryToStockCategoryDTO(stockCategory);

        restStockCategoryMockMvc.perform(post("/api/stockCategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockCategoryDTO)))
                .andExpect(status().isCreated());

        // Validate the StockCategory in the database
        List<StockCategory> stockCategorys = stockCategoryRepository.findAll();
        assertThat(stockCategorys).hasSize(databaseSizeBeforeCreate + 1);
        StockCategory testStockCategory = stockCategorys.get(stockCategorys.size() - 1);
        assertThat(testStockCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllStockCategorys() throws Exception {
        // Initialize the database
        stockCategoryRepository.saveAndFlush(stockCategory);

        // Get all the stockCategorys
        restStockCategoryMockMvc.perform(get("/api/stockCategorys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockCategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getStockCategory() throws Exception {
        // Initialize the database
        stockCategoryRepository.saveAndFlush(stockCategory);

        // Get the stockCategory
        restStockCategoryMockMvc.perform(get("/api/stockCategorys/{id}", stockCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockCategory() throws Exception {
        // Get the stockCategory
        restStockCategoryMockMvc.perform(get("/api/stockCategorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockCategory() throws Exception {
        // Initialize the database
        stockCategoryRepository.saveAndFlush(stockCategory);

		int databaseSizeBeforeUpdate = stockCategoryRepository.findAll().size();

        // Update the stockCategory
        stockCategory.setName(UPDATED_NAME);
        
        StockCategoryDTO stockCategoryDTO = stockCategoryMapper.stockCategoryToStockCategoryDTO(stockCategory);

        restStockCategoryMockMvc.perform(put("/api/stockCategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockCategoryDTO)))
                .andExpect(status().isOk());

        // Validate the StockCategory in the database
        List<StockCategory> stockCategorys = stockCategoryRepository.findAll();
        assertThat(stockCategorys).hasSize(databaseSizeBeforeUpdate);
        StockCategory testStockCategory = stockCategorys.get(stockCategorys.size() - 1);
        assertThat(testStockCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteStockCategory() throws Exception {
        // Initialize the database
        stockCategoryRepository.saveAndFlush(stockCategory);

		int databaseSizeBeforeDelete = stockCategoryRepository.findAll().size();

        // Get the stockCategory
        restStockCategoryMockMvc.perform(delete("/api/stockCategorys/{id}", stockCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StockCategory> stockCategorys = stockCategoryRepository.findAll();
        assertThat(stockCategorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
