package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.ThePapyrusPrintApp;
import com.thepapyrusprint.backend.domain.Stock;
import com.thepapyrusprint.backend.domain.Produit;
import com.thepapyrusprint.backend.repository.StockRepository;
import com.thepapyrusprint.backend.service.StockService;
import com.thepapyrusprint.backend.service.dto.StockCriteria;
import com.thepapyrusprint.backend.service.StockQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StockResource} REST controller.
 */
@SpringBootTest(classes = ThePapyrusPrintApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class StockResourceIT {

    private static final String DEFAULT_STOCK_MAX = "AAAAAAAAAA";
    private static final String UPDATED_STOCK_MAX = "BBBBBBBBBB";

    private static final String DEFAULT_STOCK_MIN = "AAAAAAAAAA";
    private static final String UPDATED_STOCK_MIN = "BBBBBBBBBB";

    private static final String DEFAULT_QUANTITE_PRODUIT = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITE_PRODUIT = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockQueryService stockQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockMockMvc;

    private Stock stock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock()
            .stockMax(DEFAULT_STOCK_MAX)
            .stockMin(DEFAULT_STOCK_MIN)
            .quantiteProduit(DEFAULT_QUANTITE_PRODUIT)
            .observation(DEFAULT_OBSERVATION);
        return stock;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createUpdatedEntity(EntityManager em) {
        Stock stock = new Stock()
            .stockMax(UPDATED_STOCK_MAX)
            .stockMin(UPDATED_STOCK_MIN)
            .quantiteProduit(UPDATED_QUANTITE_PRODUIT)
            .observation(UPDATED_OBSERVATION);
        return stock;
    }

    @BeforeEach
    public void initTest() {
        stock = createEntity(em);
    }

    @Test
    @Transactional
    public void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getStockMax()).isEqualTo(DEFAULT_STOCK_MAX);
        assertThat(testStock.getStockMin()).isEqualTo(DEFAULT_STOCK_MIN);
        assertThat(testStock.getQuantiteProduit()).isEqualTo(DEFAULT_QUANTITE_PRODUIT);
        assertThat(testStock.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    public void createStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock with an existing ID
        stock.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkQuantiteProduitIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setQuantiteProduit(null);

        // Create the Stock, which fails.

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].stockMax").value(hasItem(DEFAULT_STOCK_MAX)))
            .andExpect(jsonPath("$.[*].stockMin").value(hasItem(DEFAULT_STOCK_MIN)))
            .andExpect(jsonPath("$.[*].quantiteProduit").value(hasItem(DEFAULT_QUANTITE_PRODUIT)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }
    
    @Test
    @Transactional
    public void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.stockMax").value(DEFAULT_STOCK_MAX))
            .andExpect(jsonPath("$.stockMin").value(DEFAULT_STOCK_MIN))
            .andExpect(jsonPath("$.quantiteProduit").value(DEFAULT_QUANTITE_PRODUIT))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }


    @Test
    @Transactional
    public void getStocksByIdFiltering() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        Long id = stock.getId();

        defaultStockShouldBeFound("id.equals=" + id);
        defaultStockShouldNotBeFound("id.notEquals=" + id);

        defaultStockShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStockShouldNotBeFound("id.greaterThan=" + id);

        defaultStockShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStockShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStocksByStockMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMax equals to DEFAULT_STOCK_MAX
        defaultStockShouldBeFound("stockMax.equals=" + DEFAULT_STOCK_MAX);

        // Get all the stockList where stockMax equals to UPDATED_STOCK_MAX
        defaultStockShouldNotBeFound("stockMax.equals=" + UPDATED_STOCK_MAX);
    }

    @Test
    @Transactional
    public void getAllStocksByStockMaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMax not equals to DEFAULT_STOCK_MAX
        defaultStockShouldNotBeFound("stockMax.notEquals=" + DEFAULT_STOCK_MAX);

        // Get all the stockList where stockMax not equals to UPDATED_STOCK_MAX
        defaultStockShouldBeFound("stockMax.notEquals=" + UPDATED_STOCK_MAX);
    }

    @Test
    @Transactional
    public void getAllStocksByStockMaxIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMax in DEFAULT_STOCK_MAX or UPDATED_STOCK_MAX
        defaultStockShouldBeFound("stockMax.in=" + DEFAULT_STOCK_MAX + "," + UPDATED_STOCK_MAX);

        // Get all the stockList where stockMax equals to UPDATED_STOCK_MAX
        defaultStockShouldNotBeFound("stockMax.in=" + UPDATED_STOCK_MAX);
    }

    @Test
    @Transactional
    public void getAllStocksByStockMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMax is not null
        defaultStockShouldBeFound("stockMax.specified=true");

        // Get all the stockList where stockMax is null
        defaultStockShouldNotBeFound("stockMax.specified=false");
    }
                @Test
    @Transactional
    public void getAllStocksByStockMaxContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMax contains DEFAULT_STOCK_MAX
        defaultStockShouldBeFound("stockMax.contains=" + DEFAULT_STOCK_MAX);

        // Get all the stockList where stockMax contains UPDATED_STOCK_MAX
        defaultStockShouldNotBeFound("stockMax.contains=" + UPDATED_STOCK_MAX);
    }

    @Test
    @Transactional
    public void getAllStocksByStockMaxNotContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMax does not contain DEFAULT_STOCK_MAX
        defaultStockShouldNotBeFound("stockMax.doesNotContain=" + DEFAULT_STOCK_MAX);

        // Get all the stockList where stockMax does not contain UPDATED_STOCK_MAX
        defaultStockShouldBeFound("stockMax.doesNotContain=" + UPDATED_STOCK_MAX);
    }


    @Test
    @Transactional
    public void getAllStocksByStockMinIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMin equals to DEFAULT_STOCK_MIN
        defaultStockShouldBeFound("stockMin.equals=" + DEFAULT_STOCK_MIN);

        // Get all the stockList where stockMin equals to UPDATED_STOCK_MIN
        defaultStockShouldNotBeFound("stockMin.equals=" + UPDATED_STOCK_MIN);
    }

    @Test
    @Transactional
    public void getAllStocksByStockMinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMin not equals to DEFAULT_STOCK_MIN
        defaultStockShouldNotBeFound("stockMin.notEquals=" + DEFAULT_STOCK_MIN);

        // Get all the stockList where stockMin not equals to UPDATED_STOCK_MIN
        defaultStockShouldBeFound("stockMin.notEquals=" + UPDATED_STOCK_MIN);
    }

    @Test
    @Transactional
    public void getAllStocksByStockMinIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMin in DEFAULT_STOCK_MIN or UPDATED_STOCK_MIN
        defaultStockShouldBeFound("stockMin.in=" + DEFAULT_STOCK_MIN + "," + UPDATED_STOCK_MIN);

        // Get all the stockList where stockMin equals to UPDATED_STOCK_MIN
        defaultStockShouldNotBeFound("stockMin.in=" + UPDATED_STOCK_MIN);
    }

    @Test
    @Transactional
    public void getAllStocksByStockMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMin is not null
        defaultStockShouldBeFound("stockMin.specified=true");

        // Get all the stockList where stockMin is null
        defaultStockShouldNotBeFound("stockMin.specified=false");
    }
                @Test
    @Transactional
    public void getAllStocksByStockMinContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMin contains DEFAULT_STOCK_MIN
        defaultStockShouldBeFound("stockMin.contains=" + DEFAULT_STOCK_MIN);

        // Get all the stockList where stockMin contains UPDATED_STOCK_MIN
        defaultStockShouldNotBeFound("stockMin.contains=" + UPDATED_STOCK_MIN);
    }

    @Test
    @Transactional
    public void getAllStocksByStockMinNotContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where stockMin does not contain DEFAULT_STOCK_MIN
        defaultStockShouldNotBeFound("stockMin.doesNotContain=" + DEFAULT_STOCK_MIN);

        // Get all the stockList where stockMin does not contain UPDATED_STOCK_MIN
        defaultStockShouldBeFound("stockMin.doesNotContain=" + UPDATED_STOCK_MIN);
    }


    @Test
    @Transactional
    public void getAllStocksByQuantiteProduitIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantiteProduit equals to DEFAULT_QUANTITE_PRODUIT
        defaultStockShouldBeFound("quantiteProduit.equals=" + DEFAULT_QUANTITE_PRODUIT);

        // Get all the stockList where quantiteProduit equals to UPDATED_QUANTITE_PRODUIT
        defaultStockShouldNotBeFound("quantiteProduit.equals=" + UPDATED_QUANTITE_PRODUIT);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantiteProduitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantiteProduit not equals to DEFAULT_QUANTITE_PRODUIT
        defaultStockShouldNotBeFound("quantiteProduit.notEquals=" + DEFAULT_QUANTITE_PRODUIT);

        // Get all the stockList where quantiteProduit not equals to UPDATED_QUANTITE_PRODUIT
        defaultStockShouldBeFound("quantiteProduit.notEquals=" + UPDATED_QUANTITE_PRODUIT);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantiteProduitIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantiteProduit in DEFAULT_QUANTITE_PRODUIT or UPDATED_QUANTITE_PRODUIT
        defaultStockShouldBeFound("quantiteProduit.in=" + DEFAULT_QUANTITE_PRODUIT + "," + UPDATED_QUANTITE_PRODUIT);

        // Get all the stockList where quantiteProduit equals to UPDATED_QUANTITE_PRODUIT
        defaultStockShouldNotBeFound("quantiteProduit.in=" + UPDATED_QUANTITE_PRODUIT);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantiteProduitIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantiteProduit is not null
        defaultStockShouldBeFound("quantiteProduit.specified=true");

        // Get all the stockList where quantiteProduit is null
        defaultStockShouldNotBeFound("quantiteProduit.specified=false");
    }
                @Test
    @Transactional
    public void getAllStocksByQuantiteProduitContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantiteProduit contains DEFAULT_QUANTITE_PRODUIT
        defaultStockShouldBeFound("quantiteProduit.contains=" + DEFAULT_QUANTITE_PRODUIT);

        // Get all the stockList where quantiteProduit contains UPDATED_QUANTITE_PRODUIT
        defaultStockShouldNotBeFound("quantiteProduit.contains=" + UPDATED_QUANTITE_PRODUIT);
    }

    @Test
    @Transactional
    public void getAllStocksByQuantiteProduitNotContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where quantiteProduit does not contain DEFAULT_QUANTITE_PRODUIT
        defaultStockShouldNotBeFound("quantiteProduit.doesNotContain=" + DEFAULT_QUANTITE_PRODUIT);

        // Get all the stockList where quantiteProduit does not contain UPDATED_QUANTITE_PRODUIT
        defaultStockShouldBeFound("quantiteProduit.doesNotContain=" + UPDATED_QUANTITE_PRODUIT);
    }


    @Test
    @Transactional
    public void getAllStocksByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where observation equals to DEFAULT_OBSERVATION
        defaultStockShouldBeFound("observation.equals=" + DEFAULT_OBSERVATION);

        // Get all the stockList where observation equals to UPDATED_OBSERVATION
        defaultStockShouldNotBeFound("observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllStocksByObservationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where observation not equals to DEFAULT_OBSERVATION
        defaultStockShouldNotBeFound("observation.notEquals=" + DEFAULT_OBSERVATION);

        // Get all the stockList where observation not equals to UPDATED_OBSERVATION
        defaultStockShouldBeFound("observation.notEquals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllStocksByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where observation in DEFAULT_OBSERVATION or UPDATED_OBSERVATION
        defaultStockShouldBeFound("observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION);

        // Get all the stockList where observation equals to UPDATED_OBSERVATION
        defaultStockShouldNotBeFound("observation.in=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllStocksByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where observation is not null
        defaultStockShouldBeFound("observation.specified=true");

        // Get all the stockList where observation is null
        defaultStockShouldNotBeFound("observation.specified=false");
    }
                @Test
    @Transactional
    public void getAllStocksByObservationContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where observation contains DEFAULT_OBSERVATION
        defaultStockShouldBeFound("observation.contains=" + DEFAULT_OBSERVATION);

        // Get all the stockList where observation contains UPDATED_OBSERVATION
        defaultStockShouldNotBeFound("observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllStocksByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where observation does not contain DEFAULT_OBSERVATION
        defaultStockShouldNotBeFound("observation.doesNotContain=" + DEFAULT_OBSERVATION);

        // Get all the stockList where observation does not contain UPDATED_OBSERVATION
        defaultStockShouldBeFound("observation.doesNotContain=" + UPDATED_OBSERVATION);
    }


    @Test
    @Transactional
    public void getAllStocksByProduitIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);
        Produit produit = ProduitResourceIT.createEntity(em);
        em.persist(produit);
        em.flush();
        stock.setProduit(produit);
        stockRepository.saveAndFlush(stock);
        Long produitId = produit.getId();

        // Get all the stockList where produit equals to produitId
        defaultStockShouldBeFound("produitId.equals=" + produitId);

        // Get all the stockList where produit equals to produitId + 1
        defaultStockShouldNotBeFound("produitId.equals=" + (produitId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStockShouldBeFound(String filter) throws Exception {
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].stockMax").value(hasItem(DEFAULT_STOCK_MAX)))
            .andExpect(jsonPath("$.[*].stockMin").value(hasItem(DEFAULT_STOCK_MIN)))
            .andExpect(jsonPath("$.[*].quantiteProduit").value(hasItem(DEFAULT_QUANTITE_PRODUIT)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));

        // Check, that the count call also returns 1
        restStockMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStockShouldNotBeFound(String filter) throws Exception {
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = stockRepository.findById(stock.getId()).get();
        // Disconnect from session so that the updates on updatedStock are not directly saved in db
        em.detach(updatedStock);
        updatedStock
            .stockMax(UPDATED_STOCK_MAX)
            .stockMin(UPDATED_STOCK_MIN)
            .quantiteProduit(UPDATED_QUANTITE_PRODUIT)
            .observation(UPDATED_OBSERVATION);

        restStockMockMvc.perform(put("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStock)))
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getStockMax()).isEqualTo(UPDATED_STOCK_MAX);
        assertThat(testStock.getStockMin()).isEqualTo(UPDATED_STOCK_MIN);
        assertThat(testStock.getQuantiteProduit()).isEqualTo(UPDATED_QUANTITE_PRODUIT);
        assertThat(testStock.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void updateNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Create the Stock

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMockMvc.perform(put("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stock)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStock() throws Exception {
        // Initialize the database
        stockService.save(stock);

        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Delete the stock
        restStockMockMvc.perform(delete("/api/stocks/{id}", stock.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
