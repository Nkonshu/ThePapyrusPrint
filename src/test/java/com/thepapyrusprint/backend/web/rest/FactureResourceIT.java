package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.ThePapyrusPrintApp;
import com.thepapyrusprint.backend.domain.Facture;
import com.thepapyrusprint.backend.domain.Produit;
import com.thepapyrusprint.backend.domain.Client;
import com.thepapyrusprint.backend.repository.FactureRepository;
import com.thepapyrusprint.backend.service.FactureService;
import com.thepapyrusprint.backend.service.dto.FactureCriteria;
import com.thepapyrusprint.backend.service.FactureQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.thepapyrusprint.backend.domain.enumeration.OrderStatus;
/**
 * Integration tests for the {@link FactureResource} REST controller.
 */
@SpringBootTest(classes = ThePapyrusPrintApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class FactureResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_LIVRAISON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LIVRAISON = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_LIVRAISON = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_LIEUX_LIVRAISON = "AAAAAAAAAA";
    private static final String UPDATED_LIEUX_LIVRAISON = "BBBBBBBBBB";

    private static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.PREPARATION;
    private static final OrderStatus UPDATED_ORDER_STATUS = OrderStatus.EXPEDITION;

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    @Autowired
    private FactureRepository factureRepository;

    @Mock
    private FactureRepository factureRepositoryMock;

    @Mock
    private FactureService factureServiceMock;

    @Autowired
    private FactureService factureService;

    @Autowired
    private FactureQueryService factureQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactureMockMvc;

    private Facture facture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createEntity(EntityManager em) {
        Facture facture = new Facture()
            .date(DEFAULT_DATE)
            .numero(DEFAULT_NUMERO)
            .dateLivraison(DEFAULT_DATE_LIVRAISON)
            .lieuxLivraison(DEFAULT_LIEUX_LIVRAISON)
            .orderStatus(DEFAULT_ORDER_STATUS)
            .observation(DEFAULT_OBSERVATION);
        return facture;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createUpdatedEntity(EntityManager em) {
        Facture facture = new Facture()
            .date(UPDATED_DATE)
            .numero(UPDATED_NUMERO)
            .dateLivraison(UPDATED_DATE_LIVRAISON)
            .lieuxLivraison(UPDATED_LIEUX_LIVRAISON)
            .orderStatus(UPDATED_ORDER_STATUS)
            .observation(UPDATED_OBSERVATION);
        return facture;
    }

    @BeforeEach
    public void initTest() {
        facture = createEntity(em);
    }

    @Test
    @Transactional
    public void createFacture() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate + 1);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testFacture.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testFacture.getDateLivraison()).isEqualTo(DEFAULT_DATE_LIVRAISON);
        assertThat(testFacture.getLieuxLivraison()).isEqualTo(DEFAULT_LIEUX_LIVRAISON);
        assertThat(testFacture.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testFacture.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    public void createFactureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture with an existing ID
        facture.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDate(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setNumero(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateLivraisonIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setDateLivraison(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLieuxLivraisonIsRequired() throws Exception {
        int databaseSizeBeforeTest = factureRepository.findAll().size();
        // set the field null
        facture.setLieuxLivraison(null);

        // Create the Facture, which fails.

        restFactureMockMvc.perform(post("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFactures() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateLivraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].lieuxLivraison").value(hasItem(DEFAULT_LIEUX_LIVRAISON)))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllFacturesWithEagerRelationshipsIsEnabled() throws Exception {
        when(factureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFactureMockMvc.perform(get("/api/factures?eagerload=true"))
            .andExpect(status().isOk());

        verify(factureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllFacturesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(factureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFactureMockMvc.perform(get("/api/factures?eagerload=true"))
            .andExpect(status().isOk());

        verify(factureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.dateLivraison").value(DEFAULT_DATE_LIVRAISON.toString()))
            .andExpect(jsonPath("$.lieuxLivraison").value(DEFAULT_LIEUX_LIVRAISON))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }


    @Test
    @Transactional
    public void getFacturesByIdFiltering() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        Long id = facture.getId();

        defaultFactureShouldBeFound("id.equals=" + id);
        defaultFactureShouldNotBeFound("id.notEquals=" + id);

        defaultFactureShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFactureShouldNotBeFound("id.greaterThan=" + id);

        defaultFactureShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFactureShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFacturesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date equals to DEFAULT_DATE
        defaultFactureShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the factureList where date equals to UPDATED_DATE
        defaultFactureShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date not equals to DEFAULT_DATE
        defaultFactureShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the factureList where date not equals to UPDATED_DATE
        defaultFactureShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date in DEFAULT_DATE or UPDATED_DATE
        defaultFactureShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the factureList where date equals to UPDATED_DATE
        defaultFactureShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date is not null
        defaultFactureShouldBeFound("date.specified=true");

        // Get all the factureList where date is null
        defaultFactureShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date is greater than or equal to DEFAULT_DATE
        defaultFactureShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the factureList where date is greater than or equal to UPDATED_DATE
        defaultFactureShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date is less than or equal to DEFAULT_DATE
        defaultFactureShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the factureList where date is less than or equal to SMALLER_DATE
        defaultFactureShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date is less than DEFAULT_DATE
        defaultFactureShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the factureList where date is less than UPDATED_DATE
        defaultFactureShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where date is greater than DEFAULT_DATE
        defaultFactureShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the factureList where date is greater than SMALLER_DATE
        defaultFactureShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllFacturesByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero equals to DEFAULT_NUMERO
        defaultFactureShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the factureList where numero equals to UPDATED_NUMERO
        defaultFactureShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllFacturesByNumeroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero not equals to DEFAULT_NUMERO
        defaultFactureShouldNotBeFound("numero.notEquals=" + DEFAULT_NUMERO);

        // Get all the factureList where numero not equals to UPDATED_NUMERO
        defaultFactureShouldBeFound("numero.notEquals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllFacturesByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultFactureShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the factureList where numero equals to UPDATED_NUMERO
        defaultFactureShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllFacturesByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero is not null
        defaultFactureShouldBeFound("numero.specified=true");

        // Get all the factureList where numero is null
        defaultFactureShouldNotBeFound("numero.specified=false");
    }
                @Test
    @Transactional
    public void getAllFacturesByNumeroContainsSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero contains DEFAULT_NUMERO
        defaultFactureShouldBeFound("numero.contains=" + DEFAULT_NUMERO);

        // Get all the factureList where numero contains UPDATED_NUMERO
        defaultFactureShouldNotBeFound("numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllFacturesByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where numero does not contain DEFAULT_NUMERO
        defaultFactureShouldNotBeFound("numero.doesNotContain=" + DEFAULT_NUMERO);

        // Get all the factureList where numero does not contain UPDATED_NUMERO
        defaultFactureShouldBeFound("numero.doesNotContain=" + UPDATED_NUMERO);
    }


    @Test
    @Transactional
    public void getAllFacturesByDateLivraisonIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateLivraison equals to DEFAULT_DATE_LIVRAISON
        defaultFactureShouldBeFound("dateLivraison.equals=" + DEFAULT_DATE_LIVRAISON);

        // Get all the factureList where dateLivraison equals to UPDATED_DATE_LIVRAISON
        defaultFactureShouldNotBeFound("dateLivraison.equals=" + UPDATED_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateLivraisonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateLivraison not equals to DEFAULT_DATE_LIVRAISON
        defaultFactureShouldNotBeFound("dateLivraison.notEquals=" + DEFAULT_DATE_LIVRAISON);

        // Get all the factureList where dateLivraison not equals to UPDATED_DATE_LIVRAISON
        defaultFactureShouldBeFound("dateLivraison.notEquals=" + UPDATED_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateLivraisonIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateLivraison in DEFAULT_DATE_LIVRAISON or UPDATED_DATE_LIVRAISON
        defaultFactureShouldBeFound("dateLivraison.in=" + DEFAULT_DATE_LIVRAISON + "," + UPDATED_DATE_LIVRAISON);

        // Get all the factureList where dateLivraison equals to UPDATED_DATE_LIVRAISON
        defaultFactureShouldNotBeFound("dateLivraison.in=" + UPDATED_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateLivraisonIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateLivraison is not null
        defaultFactureShouldBeFound("dateLivraison.specified=true");

        // Get all the factureList where dateLivraison is null
        defaultFactureShouldNotBeFound("dateLivraison.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByDateLivraisonIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateLivraison is greater than or equal to DEFAULT_DATE_LIVRAISON
        defaultFactureShouldBeFound("dateLivraison.greaterThanOrEqual=" + DEFAULT_DATE_LIVRAISON);

        // Get all the factureList where dateLivraison is greater than or equal to UPDATED_DATE_LIVRAISON
        defaultFactureShouldNotBeFound("dateLivraison.greaterThanOrEqual=" + UPDATED_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateLivraisonIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateLivraison is less than or equal to DEFAULT_DATE_LIVRAISON
        defaultFactureShouldBeFound("dateLivraison.lessThanOrEqual=" + DEFAULT_DATE_LIVRAISON);

        // Get all the factureList where dateLivraison is less than or equal to SMALLER_DATE_LIVRAISON
        defaultFactureShouldNotBeFound("dateLivraison.lessThanOrEqual=" + SMALLER_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateLivraisonIsLessThanSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateLivraison is less than DEFAULT_DATE_LIVRAISON
        defaultFactureShouldNotBeFound("dateLivraison.lessThan=" + DEFAULT_DATE_LIVRAISON);

        // Get all the factureList where dateLivraison is less than UPDATED_DATE_LIVRAISON
        defaultFactureShouldBeFound("dateLivraison.lessThan=" + UPDATED_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllFacturesByDateLivraisonIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where dateLivraison is greater than DEFAULT_DATE_LIVRAISON
        defaultFactureShouldNotBeFound("dateLivraison.greaterThan=" + DEFAULT_DATE_LIVRAISON);

        // Get all the factureList where dateLivraison is greater than SMALLER_DATE_LIVRAISON
        defaultFactureShouldBeFound("dateLivraison.greaterThan=" + SMALLER_DATE_LIVRAISON);
    }


    @Test
    @Transactional
    public void getAllFacturesByLieuxLivraisonIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where lieuxLivraison equals to DEFAULT_LIEUX_LIVRAISON
        defaultFactureShouldBeFound("lieuxLivraison.equals=" + DEFAULT_LIEUX_LIVRAISON);

        // Get all the factureList where lieuxLivraison equals to UPDATED_LIEUX_LIVRAISON
        defaultFactureShouldNotBeFound("lieuxLivraison.equals=" + UPDATED_LIEUX_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllFacturesByLieuxLivraisonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where lieuxLivraison not equals to DEFAULT_LIEUX_LIVRAISON
        defaultFactureShouldNotBeFound("lieuxLivraison.notEquals=" + DEFAULT_LIEUX_LIVRAISON);

        // Get all the factureList where lieuxLivraison not equals to UPDATED_LIEUX_LIVRAISON
        defaultFactureShouldBeFound("lieuxLivraison.notEquals=" + UPDATED_LIEUX_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllFacturesByLieuxLivraisonIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where lieuxLivraison in DEFAULT_LIEUX_LIVRAISON or UPDATED_LIEUX_LIVRAISON
        defaultFactureShouldBeFound("lieuxLivraison.in=" + DEFAULT_LIEUX_LIVRAISON + "," + UPDATED_LIEUX_LIVRAISON);

        // Get all the factureList where lieuxLivraison equals to UPDATED_LIEUX_LIVRAISON
        defaultFactureShouldNotBeFound("lieuxLivraison.in=" + UPDATED_LIEUX_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllFacturesByLieuxLivraisonIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where lieuxLivraison is not null
        defaultFactureShouldBeFound("lieuxLivraison.specified=true");

        // Get all the factureList where lieuxLivraison is null
        defaultFactureShouldNotBeFound("lieuxLivraison.specified=false");
    }
                @Test
    @Transactional
    public void getAllFacturesByLieuxLivraisonContainsSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where lieuxLivraison contains DEFAULT_LIEUX_LIVRAISON
        defaultFactureShouldBeFound("lieuxLivraison.contains=" + DEFAULT_LIEUX_LIVRAISON);

        // Get all the factureList where lieuxLivraison contains UPDATED_LIEUX_LIVRAISON
        defaultFactureShouldNotBeFound("lieuxLivraison.contains=" + UPDATED_LIEUX_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllFacturesByLieuxLivraisonNotContainsSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where lieuxLivraison does not contain DEFAULT_LIEUX_LIVRAISON
        defaultFactureShouldNotBeFound("lieuxLivraison.doesNotContain=" + DEFAULT_LIEUX_LIVRAISON);

        // Get all the factureList where lieuxLivraison does not contain UPDATED_LIEUX_LIVRAISON
        defaultFactureShouldBeFound("lieuxLivraison.doesNotContain=" + UPDATED_LIEUX_LIVRAISON);
    }


    @Test
    @Transactional
    public void getAllFacturesByOrderStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where orderStatus equals to DEFAULT_ORDER_STATUS
        defaultFactureShouldBeFound("orderStatus.equals=" + DEFAULT_ORDER_STATUS);

        // Get all the factureList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultFactureShouldNotBeFound("orderStatus.equals=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllFacturesByOrderStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where orderStatus not equals to DEFAULT_ORDER_STATUS
        defaultFactureShouldNotBeFound("orderStatus.notEquals=" + DEFAULT_ORDER_STATUS);

        // Get all the factureList where orderStatus not equals to UPDATED_ORDER_STATUS
        defaultFactureShouldBeFound("orderStatus.notEquals=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllFacturesByOrderStatusIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where orderStatus in DEFAULT_ORDER_STATUS or UPDATED_ORDER_STATUS
        defaultFactureShouldBeFound("orderStatus.in=" + DEFAULT_ORDER_STATUS + "," + UPDATED_ORDER_STATUS);

        // Get all the factureList where orderStatus equals to UPDATED_ORDER_STATUS
        defaultFactureShouldNotBeFound("orderStatus.in=" + UPDATED_ORDER_STATUS);
    }

    @Test
    @Transactional
    public void getAllFacturesByOrderStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where orderStatus is not null
        defaultFactureShouldBeFound("orderStatus.specified=true");

        // Get all the factureList where orderStatus is null
        defaultFactureShouldNotBeFound("orderStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllFacturesByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where observation equals to DEFAULT_OBSERVATION
        defaultFactureShouldBeFound("observation.equals=" + DEFAULT_OBSERVATION);

        // Get all the factureList where observation equals to UPDATED_OBSERVATION
        defaultFactureShouldNotBeFound("observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllFacturesByObservationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where observation not equals to DEFAULT_OBSERVATION
        defaultFactureShouldNotBeFound("observation.notEquals=" + DEFAULT_OBSERVATION);

        // Get all the factureList where observation not equals to UPDATED_OBSERVATION
        defaultFactureShouldBeFound("observation.notEquals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllFacturesByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where observation in DEFAULT_OBSERVATION or UPDATED_OBSERVATION
        defaultFactureShouldBeFound("observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION);

        // Get all the factureList where observation equals to UPDATED_OBSERVATION
        defaultFactureShouldNotBeFound("observation.in=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllFacturesByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where observation is not null
        defaultFactureShouldBeFound("observation.specified=true");

        // Get all the factureList where observation is null
        defaultFactureShouldNotBeFound("observation.specified=false");
    }
                @Test
    @Transactional
    public void getAllFacturesByObservationContainsSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where observation contains DEFAULT_OBSERVATION
        defaultFactureShouldBeFound("observation.contains=" + DEFAULT_OBSERVATION);

        // Get all the factureList where observation contains UPDATED_OBSERVATION
        defaultFactureShouldNotBeFound("observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllFacturesByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get all the factureList where observation does not contain DEFAULT_OBSERVATION
        defaultFactureShouldNotBeFound("observation.doesNotContain=" + DEFAULT_OBSERVATION);

        // Get all the factureList where observation does not contain UPDATED_OBSERVATION
        defaultFactureShouldBeFound("observation.doesNotContain=" + UPDATED_OBSERVATION);
    }


    @Test
    @Transactional
    public void getAllFacturesByProduitIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        Produit produit = ProduitResourceIT.createEntity(em);
        em.persist(produit);
        em.flush();
        facture.addProduit(produit);
        factureRepository.saveAndFlush(facture);
        Long produitId = produit.getId();

        // Get all the factureList where produit equals to produitId
        defaultFactureShouldBeFound("produitId.equals=" + produitId);

        // Get all the factureList where produit equals to produitId + 1
        defaultFactureShouldNotBeFound("produitId.equals=" + (produitId + 1));
    }


    @Test
    @Transactional
    public void getAllFacturesByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);
        Client client = ClientResourceIT.createEntity(em);
        em.persist(client);
        em.flush();
        facture.setClient(client);
        factureRepository.saveAndFlush(facture);
        Long clientId = client.getId();

        // Get all the factureList where client equals to clientId
        defaultFactureShouldBeFound("clientId.equals=" + clientId);

        // Get all the factureList where client equals to clientId + 1
        defaultFactureShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFactureShouldBeFound(String filter) throws Exception {
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facture.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateLivraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].lieuxLivraison").value(hasItem(DEFAULT_LIEUX_LIVRAISON)))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));

        // Check, that the count call also returns 1
        restFactureMockMvc.perform(get("/api/factures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFactureShouldNotBeFound(String filter) throws Exception {
        restFactureMockMvc.perform(get("/api/factures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFactureMockMvc.perform(get("/api/factures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFacture() throws Exception {
        // Initialize the database
        factureService.save(facture);

        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture
        Facture updatedFacture = factureRepository.findById(facture.getId()).get();
        // Disconnect from session so that the updates on updatedFacture are not directly saved in db
        em.detach(updatedFacture);
        updatedFacture
            .date(UPDATED_DATE)
            .numero(UPDATED_NUMERO)
            .dateLivraison(UPDATED_DATE_LIVRAISON)
            .lieuxLivraison(UPDATED_LIEUX_LIVRAISON)
            .orderStatus(UPDATED_ORDER_STATUS)
            .observation(UPDATED_OBSERVATION);

        restFactureMockMvc.perform(put("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFacture)))
            .andExpect(status().isOk());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testFacture.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testFacture.getDateLivraison()).isEqualTo(UPDATED_DATE_LIVRAISON);
        assertThat(testFacture.getLieuxLivraison()).isEqualTo(UPDATED_LIEUX_LIVRAISON);
        assertThat(testFacture.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testFacture.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void updateNonExistingFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Create the Facture

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc.perform(put("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFacture() throws Exception {
        // Initialize the database
        factureService.save(facture);

        int databaseSizeBeforeDelete = factureRepository.findAll().size();

        // Delete the facture
        restFactureMockMvc.perform(delete("/api/factures/{id}", facture.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
