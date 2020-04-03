package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.ThePapyrusPrintApp;
import com.thepapyrusprint.backend.domain.Produit;
import com.thepapyrusprint.backend.domain.Notation;
import com.thepapyrusprint.backend.domain.Image;
import com.thepapyrusprint.backend.domain.Marque;
import com.thepapyrusprint.backend.domain.Catalogue;
import com.thepapyrusprint.backend.domain.Commande;
import com.thepapyrusprint.backend.domain.Facture;
import com.thepapyrusprint.backend.repository.ProduitRepository;
import com.thepapyrusprint.backend.service.ProduitService;
import com.thepapyrusprint.backend.service.dto.ProduitCriteria;
import com.thepapyrusprint.backend.service.ProduitQueryService;

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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProduitResource} REST controller.
 */
@SpringBootTest(classes = ThePapyrusPrintApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProduitResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Float DEFAULT_PRIX_VENTE_HTVA = 1F;
    private static final Float UPDATED_PRIX_VENTE_HTVA = 2F;
    private static final Float SMALLER_PRIX_VENTE_HTVA = 1F - 1F;

    private static final Float DEFAULT_PRIX_VENTE_TVAC = 1F;
    private static final Float UPDATED_PRIX_VENTE_TVAC = 2F;
    private static final Float SMALLER_PRIX_VENTE_TVAC = 1F - 1F;

    private static final Float DEFAULT_PRIX_ACHAT_HT = 1F;
    private static final Float UPDATED_PRIX_ACHAT_HT = 2F;
    private static final Float SMALLER_PRIX_ACHAT_HT = 1F - 1F;

    private static final Float DEFAULT_TAUX_TVA_ACHAT = 1F;
    private static final Float UPDATED_TAUX_TVA_ACHAT = 2F;
    private static final Float SMALLER_TAUX_TVA_ACHAT = 1F - 1F;

    private static final Float DEFAULT_TAUX_TVA = 1F;
    private static final Float UPDATED_TAUX_TVA = 2F;
    private static final Float SMALLER_TAUX_TVA = 1F - 1F;

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ProduitRepository produitRepository;

    @Mock
    private ProduitRepository produitRepositoryMock;

    @Mock
    private ProduitService produitServiceMock;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private ProduitQueryService produitQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProduitMockMvc;

    private Produit produit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produit createEntity(EntityManager em) {
        Produit produit = new Produit()
            .nom(DEFAULT_NOM)
            .prixVenteHtva(DEFAULT_PRIX_VENTE_HTVA)
            .prixVenteTvac(DEFAULT_PRIX_VENTE_TVAC)
            .prixAchatHt(DEFAULT_PRIX_ACHAT_HT)
            .tauxTvaAchat(DEFAULT_TAUX_TVA_ACHAT)
            .tauxTva(DEFAULT_TAUX_TVA)
            .observation(DEFAULT_OBSERVATION)
            .description(DEFAULT_DESCRIPTION);
        return produit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Produit createUpdatedEntity(EntityManager em) {
        Produit produit = new Produit()
            .nom(UPDATED_NOM)
            .prixVenteHtva(UPDATED_PRIX_VENTE_HTVA)
            .prixVenteTvac(UPDATED_PRIX_VENTE_TVAC)
            .prixAchatHt(UPDATED_PRIX_ACHAT_HT)
            .tauxTvaAchat(UPDATED_TAUX_TVA_ACHAT)
            .tauxTva(UPDATED_TAUX_TVA)
            .observation(UPDATED_OBSERVATION)
            .description(UPDATED_DESCRIPTION);
        return produit;
    }

    @BeforeEach
    public void initTest() {
        produit = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduit() throws Exception {
        int databaseSizeBeforeCreate = produitRepository.findAll().size();

        // Create the Produit
        restProduitMockMvc.perform(post("/api/produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isCreated());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeCreate + 1);
        Produit testProduit = produitList.get(produitList.size() - 1);
        assertThat(testProduit.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testProduit.getPrixVenteHtva()).isEqualTo(DEFAULT_PRIX_VENTE_HTVA);
        assertThat(testProduit.getPrixVenteTvac()).isEqualTo(DEFAULT_PRIX_VENTE_TVAC);
        assertThat(testProduit.getPrixAchatHt()).isEqualTo(DEFAULT_PRIX_ACHAT_HT);
        assertThat(testProduit.getTauxTvaAchat()).isEqualTo(DEFAULT_TAUX_TVA_ACHAT);
        assertThat(testProduit.getTauxTva()).isEqualTo(DEFAULT_TAUX_TVA);
        assertThat(testProduit.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testProduit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createProduitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = produitRepository.findAll().size();

        // Create the Produit with an existing ID
        produit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProduitMockMvc.perform(post("/api/produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setNom(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isBadRequest());

        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixVenteHtvaIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setPrixVenteHtva(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isBadRequest());

        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixVenteTvacIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setPrixVenteTvac(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isBadRequest());

        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixAchatHtIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setPrixAchatHt(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isBadRequest());

        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTauxTvaAchatIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setTauxTvaAchat(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isBadRequest());

        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTauxTvaIsRequired() throws Exception {
        int databaseSizeBeforeTest = produitRepository.findAll().size();
        // set the field null
        produit.setTauxTva(null);

        // Create the Produit, which fails.

        restProduitMockMvc.perform(post("/api/produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isBadRequest());

        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProduits() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList
        restProduitMockMvc.perform(get("/api/produits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produit.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prixVenteHtva").value(hasItem(DEFAULT_PRIX_VENTE_HTVA.doubleValue())))
            .andExpect(jsonPath("$.[*].prixVenteTvac").value(hasItem(DEFAULT_PRIX_VENTE_TVAC.doubleValue())))
            .andExpect(jsonPath("$.[*].prixAchatHt").value(hasItem(DEFAULT_PRIX_ACHAT_HT.doubleValue())))
            .andExpect(jsonPath("$.[*].tauxTvaAchat").value(hasItem(DEFAULT_TAUX_TVA_ACHAT.doubleValue())))
            .andExpect(jsonPath("$.[*].tauxTva").value(hasItem(DEFAULT_TAUX_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllProduitsWithEagerRelationshipsIsEnabled() throws Exception {
        when(produitServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProduitMockMvc.perform(get("/api/produits?eagerload=true"))
            .andExpect(status().isOk());

        verify(produitServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllProduitsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(produitServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProduitMockMvc.perform(get("/api/produits?eagerload=true"))
            .andExpect(status().isOk());

        verify(produitServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProduit() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get the produit
        restProduitMockMvc.perform(get("/api/produits/{id}", produit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(produit.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prixVenteHtva").value(DEFAULT_PRIX_VENTE_HTVA.doubleValue()))
            .andExpect(jsonPath("$.prixVenteTvac").value(DEFAULT_PRIX_VENTE_TVAC.doubleValue()))
            .andExpect(jsonPath("$.prixAchatHt").value(DEFAULT_PRIX_ACHAT_HT.doubleValue()))
            .andExpect(jsonPath("$.tauxTvaAchat").value(DEFAULT_TAUX_TVA_ACHAT.doubleValue()))
            .andExpect(jsonPath("$.tauxTva").value(DEFAULT_TAUX_TVA.doubleValue()))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getProduitsByIdFiltering() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        Long id = produit.getId();

        defaultProduitShouldBeFound("id.equals=" + id);
        defaultProduitShouldNotBeFound("id.notEquals=" + id);

        defaultProduitShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProduitShouldNotBeFound("id.greaterThan=" + id);

        defaultProduitShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProduitShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProduitsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where nom equals to DEFAULT_NOM
        defaultProduitShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the produitList where nom equals to UPDATED_NOM
        defaultProduitShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllProduitsByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where nom not equals to DEFAULT_NOM
        defaultProduitShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the produitList where nom not equals to UPDATED_NOM
        defaultProduitShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllProduitsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultProduitShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the produitList where nom equals to UPDATED_NOM
        defaultProduitShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllProduitsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where nom is not null
        defaultProduitShouldBeFound("nom.specified=true");

        // Get all the produitList where nom is null
        defaultProduitShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllProduitsByNomContainsSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where nom contains DEFAULT_NOM
        defaultProduitShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the produitList where nom contains UPDATED_NOM
        defaultProduitShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllProduitsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where nom does not contain DEFAULT_NOM
        defaultProduitShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the produitList where nom does not contain UPDATED_NOM
        defaultProduitShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllProduitsByPrixVenteHtvaIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteHtva equals to DEFAULT_PRIX_VENTE_HTVA
        defaultProduitShouldBeFound("prixVenteHtva.equals=" + DEFAULT_PRIX_VENTE_HTVA);

        // Get all the produitList where prixVenteHtva equals to UPDATED_PRIX_VENTE_HTVA
        defaultProduitShouldNotBeFound("prixVenteHtva.equals=" + UPDATED_PRIX_VENTE_HTVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteHtvaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteHtva not equals to DEFAULT_PRIX_VENTE_HTVA
        defaultProduitShouldNotBeFound("prixVenteHtva.notEquals=" + DEFAULT_PRIX_VENTE_HTVA);

        // Get all the produitList where prixVenteHtva not equals to UPDATED_PRIX_VENTE_HTVA
        defaultProduitShouldBeFound("prixVenteHtva.notEquals=" + UPDATED_PRIX_VENTE_HTVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteHtvaIsInShouldWork() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteHtva in DEFAULT_PRIX_VENTE_HTVA or UPDATED_PRIX_VENTE_HTVA
        defaultProduitShouldBeFound("prixVenteHtva.in=" + DEFAULT_PRIX_VENTE_HTVA + "," + UPDATED_PRIX_VENTE_HTVA);

        // Get all the produitList where prixVenteHtva equals to UPDATED_PRIX_VENTE_HTVA
        defaultProduitShouldNotBeFound("prixVenteHtva.in=" + UPDATED_PRIX_VENTE_HTVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteHtvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteHtva is not null
        defaultProduitShouldBeFound("prixVenteHtva.specified=true");

        // Get all the produitList where prixVenteHtva is null
        defaultProduitShouldNotBeFound("prixVenteHtva.specified=false");
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteHtvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteHtva is greater than or equal to DEFAULT_PRIX_VENTE_HTVA
        defaultProduitShouldBeFound("prixVenteHtva.greaterThanOrEqual=" + DEFAULT_PRIX_VENTE_HTVA);

        // Get all the produitList where prixVenteHtva is greater than or equal to UPDATED_PRIX_VENTE_HTVA
        defaultProduitShouldNotBeFound("prixVenteHtva.greaterThanOrEqual=" + UPDATED_PRIX_VENTE_HTVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteHtvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteHtva is less than or equal to DEFAULT_PRIX_VENTE_HTVA
        defaultProduitShouldBeFound("prixVenteHtva.lessThanOrEqual=" + DEFAULT_PRIX_VENTE_HTVA);

        // Get all the produitList where prixVenteHtva is less than or equal to SMALLER_PRIX_VENTE_HTVA
        defaultProduitShouldNotBeFound("prixVenteHtva.lessThanOrEqual=" + SMALLER_PRIX_VENTE_HTVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteHtvaIsLessThanSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteHtva is less than DEFAULT_PRIX_VENTE_HTVA
        defaultProduitShouldNotBeFound("prixVenteHtva.lessThan=" + DEFAULT_PRIX_VENTE_HTVA);

        // Get all the produitList where prixVenteHtva is less than UPDATED_PRIX_VENTE_HTVA
        defaultProduitShouldBeFound("prixVenteHtva.lessThan=" + UPDATED_PRIX_VENTE_HTVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteHtvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteHtva is greater than DEFAULT_PRIX_VENTE_HTVA
        defaultProduitShouldNotBeFound("prixVenteHtva.greaterThan=" + DEFAULT_PRIX_VENTE_HTVA);

        // Get all the produitList where prixVenteHtva is greater than SMALLER_PRIX_VENTE_HTVA
        defaultProduitShouldBeFound("prixVenteHtva.greaterThan=" + SMALLER_PRIX_VENTE_HTVA);
    }


    @Test
    @Transactional
    public void getAllProduitsByPrixVenteTvacIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteTvac equals to DEFAULT_PRIX_VENTE_TVAC
        defaultProduitShouldBeFound("prixVenteTvac.equals=" + DEFAULT_PRIX_VENTE_TVAC);

        // Get all the produitList where prixVenteTvac equals to UPDATED_PRIX_VENTE_TVAC
        defaultProduitShouldNotBeFound("prixVenteTvac.equals=" + UPDATED_PRIX_VENTE_TVAC);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteTvacIsNotEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteTvac not equals to DEFAULT_PRIX_VENTE_TVAC
        defaultProduitShouldNotBeFound("prixVenteTvac.notEquals=" + DEFAULT_PRIX_VENTE_TVAC);

        // Get all the produitList where prixVenteTvac not equals to UPDATED_PRIX_VENTE_TVAC
        defaultProduitShouldBeFound("prixVenteTvac.notEquals=" + UPDATED_PRIX_VENTE_TVAC);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteTvacIsInShouldWork() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteTvac in DEFAULT_PRIX_VENTE_TVAC or UPDATED_PRIX_VENTE_TVAC
        defaultProduitShouldBeFound("prixVenteTvac.in=" + DEFAULT_PRIX_VENTE_TVAC + "," + UPDATED_PRIX_VENTE_TVAC);

        // Get all the produitList where prixVenteTvac equals to UPDATED_PRIX_VENTE_TVAC
        defaultProduitShouldNotBeFound("prixVenteTvac.in=" + UPDATED_PRIX_VENTE_TVAC);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteTvacIsNullOrNotNull() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteTvac is not null
        defaultProduitShouldBeFound("prixVenteTvac.specified=true");

        // Get all the produitList where prixVenteTvac is null
        defaultProduitShouldNotBeFound("prixVenteTvac.specified=false");
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteTvacIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteTvac is greater than or equal to DEFAULT_PRIX_VENTE_TVAC
        defaultProduitShouldBeFound("prixVenteTvac.greaterThanOrEqual=" + DEFAULT_PRIX_VENTE_TVAC);

        // Get all the produitList where prixVenteTvac is greater than or equal to UPDATED_PRIX_VENTE_TVAC
        defaultProduitShouldNotBeFound("prixVenteTvac.greaterThanOrEqual=" + UPDATED_PRIX_VENTE_TVAC);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteTvacIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteTvac is less than or equal to DEFAULT_PRIX_VENTE_TVAC
        defaultProduitShouldBeFound("prixVenteTvac.lessThanOrEqual=" + DEFAULT_PRIX_VENTE_TVAC);

        // Get all the produitList where prixVenteTvac is less than or equal to SMALLER_PRIX_VENTE_TVAC
        defaultProduitShouldNotBeFound("prixVenteTvac.lessThanOrEqual=" + SMALLER_PRIX_VENTE_TVAC);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteTvacIsLessThanSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteTvac is less than DEFAULT_PRIX_VENTE_TVAC
        defaultProduitShouldNotBeFound("prixVenteTvac.lessThan=" + DEFAULT_PRIX_VENTE_TVAC);

        // Get all the produitList where prixVenteTvac is less than UPDATED_PRIX_VENTE_TVAC
        defaultProduitShouldBeFound("prixVenteTvac.lessThan=" + UPDATED_PRIX_VENTE_TVAC);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixVenteTvacIsGreaterThanSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixVenteTvac is greater than DEFAULT_PRIX_VENTE_TVAC
        defaultProduitShouldNotBeFound("prixVenteTvac.greaterThan=" + DEFAULT_PRIX_VENTE_TVAC);

        // Get all the produitList where prixVenteTvac is greater than SMALLER_PRIX_VENTE_TVAC
        defaultProduitShouldBeFound("prixVenteTvac.greaterThan=" + SMALLER_PRIX_VENTE_TVAC);
    }


    @Test
    @Transactional
    public void getAllProduitsByPrixAchatHtIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixAchatHt equals to DEFAULT_PRIX_ACHAT_HT
        defaultProduitShouldBeFound("prixAchatHt.equals=" + DEFAULT_PRIX_ACHAT_HT);

        // Get all the produitList where prixAchatHt equals to UPDATED_PRIX_ACHAT_HT
        defaultProduitShouldNotBeFound("prixAchatHt.equals=" + UPDATED_PRIX_ACHAT_HT);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixAchatHtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixAchatHt not equals to DEFAULT_PRIX_ACHAT_HT
        defaultProduitShouldNotBeFound("prixAchatHt.notEquals=" + DEFAULT_PRIX_ACHAT_HT);

        // Get all the produitList where prixAchatHt not equals to UPDATED_PRIX_ACHAT_HT
        defaultProduitShouldBeFound("prixAchatHt.notEquals=" + UPDATED_PRIX_ACHAT_HT);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixAchatHtIsInShouldWork() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixAchatHt in DEFAULT_PRIX_ACHAT_HT or UPDATED_PRIX_ACHAT_HT
        defaultProduitShouldBeFound("prixAchatHt.in=" + DEFAULT_PRIX_ACHAT_HT + "," + UPDATED_PRIX_ACHAT_HT);

        // Get all the produitList where prixAchatHt equals to UPDATED_PRIX_ACHAT_HT
        defaultProduitShouldNotBeFound("prixAchatHt.in=" + UPDATED_PRIX_ACHAT_HT);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixAchatHtIsNullOrNotNull() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixAchatHt is not null
        defaultProduitShouldBeFound("prixAchatHt.specified=true");

        // Get all the produitList where prixAchatHt is null
        defaultProduitShouldNotBeFound("prixAchatHt.specified=false");
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixAchatHtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixAchatHt is greater than or equal to DEFAULT_PRIX_ACHAT_HT
        defaultProduitShouldBeFound("prixAchatHt.greaterThanOrEqual=" + DEFAULT_PRIX_ACHAT_HT);

        // Get all the produitList where prixAchatHt is greater than or equal to UPDATED_PRIX_ACHAT_HT
        defaultProduitShouldNotBeFound("prixAchatHt.greaterThanOrEqual=" + UPDATED_PRIX_ACHAT_HT);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixAchatHtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixAchatHt is less than or equal to DEFAULT_PRIX_ACHAT_HT
        defaultProduitShouldBeFound("prixAchatHt.lessThanOrEqual=" + DEFAULT_PRIX_ACHAT_HT);

        // Get all the produitList where prixAchatHt is less than or equal to SMALLER_PRIX_ACHAT_HT
        defaultProduitShouldNotBeFound("prixAchatHt.lessThanOrEqual=" + SMALLER_PRIX_ACHAT_HT);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixAchatHtIsLessThanSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixAchatHt is less than DEFAULT_PRIX_ACHAT_HT
        defaultProduitShouldNotBeFound("prixAchatHt.lessThan=" + DEFAULT_PRIX_ACHAT_HT);

        // Get all the produitList where prixAchatHt is less than UPDATED_PRIX_ACHAT_HT
        defaultProduitShouldBeFound("prixAchatHt.lessThan=" + UPDATED_PRIX_ACHAT_HT);
    }

    @Test
    @Transactional
    public void getAllProduitsByPrixAchatHtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where prixAchatHt is greater than DEFAULT_PRIX_ACHAT_HT
        defaultProduitShouldNotBeFound("prixAchatHt.greaterThan=" + DEFAULT_PRIX_ACHAT_HT);

        // Get all the produitList where prixAchatHt is greater than SMALLER_PRIX_ACHAT_HT
        defaultProduitShouldBeFound("prixAchatHt.greaterThan=" + SMALLER_PRIX_ACHAT_HT);
    }


    @Test
    @Transactional
    public void getAllProduitsByTauxTvaAchatIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTvaAchat equals to DEFAULT_TAUX_TVA_ACHAT
        defaultProduitShouldBeFound("tauxTvaAchat.equals=" + DEFAULT_TAUX_TVA_ACHAT);

        // Get all the produitList where tauxTvaAchat equals to UPDATED_TAUX_TVA_ACHAT
        defaultProduitShouldNotBeFound("tauxTvaAchat.equals=" + UPDATED_TAUX_TVA_ACHAT);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaAchatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTvaAchat not equals to DEFAULT_TAUX_TVA_ACHAT
        defaultProduitShouldNotBeFound("tauxTvaAchat.notEquals=" + DEFAULT_TAUX_TVA_ACHAT);

        // Get all the produitList where tauxTvaAchat not equals to UPDATED_TAUX_TVA_ACHAT
        defaultProduitShouldBeFound("tauxTvaAchat.notEquals=" + UPDATED_TAUX_TVA_ACHAT);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaAchatIsInShouldWork() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTvaAchat in DEFAULT_TAUX_TVA_ACHAT or UPDATED_TAUX_TVA_ACHAT
        defaultProduitShouldBeFound("tauxTvaAchat.in=" + DEFAULT_TAUX_TVA_ACHAT + "," + UPDATED_TAUX_TVA_ACHAT);

        // Get all the produitList where tauxTvaAchat equals to UPDATED_TAUX_TVA_ACHAT
        defaultProduitShouldNotBeFound("tauxTvaAchat.in=" + UPDATED_TAUX_TVA_ACHAT);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaAchatIsNullOrNotNull() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTvaAchat is not null
        defaultProduitShouldBeFound("tauxTvaAchat.specified=true");

        // Get all the produitList where tauxTvaAchat is null
        defaultProduitShouldNotBeFound("tauxTvaAchat.specified=false");
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaAchatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTvaAchat is greater than or equal to DEFAULT_TAUX_TVA_ACHAT
        defaultProduitShouldBeFound("tauxTvaAchat.greaterThanOrEqual=" + DEFAULT_TAUX_TVA_ACHAT);

        // Get all the produitList where tauxTvaAchat is greater than or equal to UPDATED_TAUX_TVA_ACHAT
        defaultProduitShouldNotBeFound("tauxTvaAchat.greaterThanOrEqual=" + UPDATED_TAUX_TVA_ACHAT);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaAchatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTvaAchat is less than or equal to DEFAULT_TAUX_TVA_ACHAT
        defaultProduitShouldBeFound("tauxTvaAchat.lessThanOrEqual=" + DEFAULT_TAUX_TVA_ACHAT);

        // Get all the produitList where tauxTvaAchat is less than or equal to SMALLER_TAUX_TVA_ACHAT
        defaultProduitShouldNotBeFound("tauxTvaAchat.lessThanOrEqual=" + SMALLER_TAUX_TVA_ACHAT);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaAchatIsLessThanSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTvaAchat is less than DEFAULT_TAUX_TVA_ACHAT
        defaultProduitShouldNotBeFound("tauxTvaAchat.lessThan=" + DEFAULT_TAUX_TVA_ACHAT);

        // Get all the produitList where tauxTvaAchat is less than UPDATED_TAUX_TVA_ACHAT
        defaultProduitShouldBeFound("tauxTvaAchat.lessThan=" + UPDATED_TAUX_TVA_ACHAT);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaAchatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTvaAchat is greater than DEFAULT_TAUX_TVA_ACHAT
        defaultProduitShouldNotBeFound("tauxTvaAchat.greaterThan=" + DEFAULT_TAUX_TVA_ACHAT);

        // Get all the produitList where tauxTvaAchat is greater than SMALLER_TAUX_TVA_ACHAT
        defaultProduitShouldBeFound("tauxTvaAchat.greaterThan=" + SMALLER_TAUX_TVA_ACHAT);
    }


    @Test
    @Transactional
    public void getAllProduitsByTauxTvaIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva equals to DEFAULT_TAUX_TVA
        defaultProduitShouldBeFound("tauxTva.equals=" + DEFAULT_TAUX_TVA);

        // Get all the produitList where tauxTva equals to UPDATED_TAUX_TVA
        defaultProduitShouldNotBeFound("tauxTva.equals=" + UPDATED_TAUX_TVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva not equals to DEFAULT_TAUX_TVA
        defaultProduitShouldNotBeFound("tauxTva.notEquals=" + DEFAULT_TAUX_TVA);

        // Get all the produitList where tauxTva not equals to UPDATED_TAUX_TVA
        defaultProduitShouldBeFound("tauxTva.notEquals=" + UPDATED_TAUX_TVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaIsInShouldWork() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva in DEFAULT_TAUX_TVA or UPDATED_TAUX_TVA
        defaultProduitShouldBeFound("tauxTva.in=" + DEFAULT_TAUX_TVA + "," + UPDATED_TAUX_TVA);

        // Get all the produitList where tauxTva equals to UPDATED_TAUX_TVA
        defaultProduitShouldNotBeFound("tauxTva.in=" + UPDATED_TAUX_TVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva is not null
        defaultProduitShouldBeFound("tauxTva.specified=true");

        // Get all the produitList where tauxTva is null
        defaultProduitShouldNotBeFound("tauxTva.specified=false");
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva is greater than or equal to DEFAULT_TAUX_TVA
        defaultProduitShouldBeFound("tauxTva.greaterThanOrEqual=" + DEFAULT_TAUX_TVA);

        // Get all the produitList where tauxTva is greater than or equal to UPDATED_TAUX_TVA
        defaultProduitShouldNotBeFound("tauxTva.greaterThanOrEqual=" + UPDATED_TAUX_TVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva is less than or equal to DEFAULT_TAUX_TVA
        defaultProduitShouldBeFound("tauxTva.lessThanOrEqual=" + DEFAULT_TAUX_TVA);

        // Get all the produitList where tauxTva is less than or equal to SMALLER_TAUX_TVA
        defaultProduitShouldNotBeFound("tauxTva.lessThanOrEqual=" + SMALLER_TAUX_TVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaIsLessThanSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva is less than DEFAULT_TAUX_TVA
        defaultProduitShouldNotBeFound("tauxTva.lessThan=" + DEFAULT_TAUX_TVA);

        // Get all the produitList where tauxTva is less than UPDATED_TAUX_TVA
        defaultProduitShouldBeFound("tauxTva.lessThan=" + UPDATED_TAUX_TVA);
    }

    @Test
    @Transactional
    public void getAllProduitsByTauxTvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where tauxTva is greater than DEFAULT_TAUX_TVA
        defaultProduitShouldNotBeFound("tauxTva.greaterThan=" + DEFAULT_TAUX_TVA);

        // Get all the produitList where tauxTva is greater than SMALLER_TAUX_TVA
        defaultProduitShouldBeFound("tauxTva.greaterThan=" + SMALLER_TAUX_TVA);
    }


    @Test
    @Transactional
    public void getAllProduitsByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where observation equals to DEFAULT_OBSERVATION
        defaultProduitShouldBeFound("observation.equals=" + DEFAULT_OBSERVATION);

        // Get all the produitList where observation equals to UPDATED_OBSERVATION
        defaultProduitShouldNotBeFound("observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllProduitsByObservationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where observation not equals to DEFAULT_OBSERVATION
        defaultProduitShouldNotBeFound("observation.notEquals=" + DEFAULT_OBSERVATION);

        // Get all the produitList where observation not equals to UPDATED_OBSERVATION
        defaultProduitShouldBeFound("observation.notEquals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllProduitsByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where observation in DEFAULT_OBSERVATION or UPDATED_OBSERVATION
        defaultProduitShouldBeFound("observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION);

        // Get all the produitList where observation equals to UPDATED_OBSERVATION
        defaultProduitShouldNotBeFound("observation.in=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllProduitsByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where observation is not null
        defaultProduitShouldBeFound("observation.specified=true");

        // Get all the produitList where observation is null
        defaultProduitShouldNotBeFound("observation.specified=false");
    }
                @Test
    @Transactional
    public void getAllProduitsByObservationContainsSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where observation contains DEFAULT_OBSERVATION
        defaultProduitShouldBeFound("observation.contains=" + DEFAULT_OBSERVATION);

        // Get all the produitList where observation contains UPDATED_OBSERVATION
        defaultProduitShouldNotBeFound("observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllProduitsByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where observation does not contain DEFAULT_OBSERVATION
        defaultProduitShouldNotBeFound("observation.doesNotContain=" + DEFAULT_OBSERVATION);

        // Get all the produitList where observation does not contain UPDATED_OBSERVATION
        defaultProduitShouldBeFound("observation.doesNotContain=" + UPDATED_OBSERVATION);
    }


    @Test
    @Transactional
    public void getAllProduitsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where description equals to DEFAULT_DESCRIPTION
        defaultProduitShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the produitList where description equals to UPDATED_DESCRIPTION
        defaultProduitShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProduitsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where description not equals to DEFAULT_DESCRIPTION
        defaultProduitShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the produitList where description not equals to UPDATED_DESCRIPTION
        defaultProduitShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProduitsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProduitShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the produitList where description equals to UPDATED_DESCRIPTION
        defaultProduitShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProduitsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where description is not null
        defaultProduitShouldBeFound("description.specified=true");

        // Get all the produitList where description is null
        defaultProduitShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllProduitsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where description contains DEFAULT_DESCRIPTION
        defaultProduitShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the produitList where description contains UPDATED_DESCRIPTION
        defaultProduitShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProduitsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);

        // Get all the produitList where description does not contain DEFAULT_DESCRIPTION
        defaultProduitShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the produitList where description does not contain UPDATED_DESCRIPTION
        defaultProduitShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllProduitsByNotationIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);
        Notation notation = NotationResourceIT.createEntity(em);
        em.persist(notation);
        em.flush();
        produit.addNotation(notation);
        produitRepository.saveAndFlush(produit);
        Long notationId = notation.getId();

        // Get all the produitList where notation equals to notationId
        defaultProduitShouldBeFound("notationId.equals=" + notationId);

        // Get all the produitList where notation equals to notationId + 1
        defaultProduitShouldNotBeFound("notationId.equals=" + (notationId + 1));
    }


    @Test
    @Transactional
    public void getAllProduitsByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);
        Image image = ImageResourceIT.createEntity(em);
        em.persist(image);
        em.flush();
        produit.addImage(image);
        produitRepository.saveAndFlush(produit);
        Long imageId = image.getId();

        // Get all the produitList where image equals to imageId
        defaultProduitShouldBeFound("imageId.equals=" + imageId);

        // Get all the produitList where image equals to imageId + 1
        defaultProduitShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }


    @Test
    @Transactional
    public void getAllProduitsByMarqueIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);
        Marque marque = MarqueResourceIT.createEntity(em);
        em.persist(marque);
        em.flush();
        produit.setMarque(marque);
        produitRepository.saveAndFlush(produit);
        Long marqueId = marque.getId();

        // Get all the produitList where marque equals to marqueId
        defaultProduitShouldBeFound("marqueId.equals=" + marqueId);

        // Get all the produitList where marque equals to marqueId + 1
        defaultProduitShouldNotBeFound("marqueId.equals=" + (marqueId + 1));
    }


    @Test
    @Transactional
    public void getAllProduitsByCatalogueIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);
        Catalogue catalogue = CatalogueResourceIT.createEntity(em);
        em.persist(catalogue);
        em.flush();
        produit.addCatalogue(catalogue);
        produitRepository.saveAndFlush(produit);
        Long catalogueId = catalogue.getId();

        // Get all the produitList where catalogue equals to catalogueId
        defaultProduitShouldBeFound("catalogueId.equals=" + catalogueId);

        // Get all the produitList where catalogue equals to catalogueId + 1
        defaultProduitShouldNotBeFound("catalogueId.equals=" + (catalogueId + 1));
    }


    @Test
    @Transactional
    public void getAllProduitsByCommandeIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);
        Commande commande = CommandeResourceIT.createEntity(em);
        em.persist(commande);
        em.flush();
        produit.addCommande(commande);
        produitRepository.saveAndFlush(produit);
        Long commandeId = commande.getId();

        // Get all the produitList where commande equals to commandeId
        defaultProduitShouldBeFound("commandeId.equals=" + commandeId);

        // Get all the produitList where commande equals to commandeId + 1
        defaultProduitShouldNotBeFound("commandeId.equals=" + (commandeId + 1));
    }


    @Test
    @Transactional
    public void getAllProduitsByFactureIsEqualToSomething() throws Exception {
        // Initialize the database
        produitRepository.saveAndFlush(produit);
        Facture facture = FactureResourceIT.createEntity(em);
        em.persist(facture);
        em.flush();
        produit.addFacture(facture);
        produitRepository.saveAndFlush(produit);
        Long factureId = facture.getId();

        // Get all the produitList where facture equals to factureId
        defaultProduitShouldBeFound("factureId.equals=" + factureId);

        // Get all the produitList where facture equals to factureId + 1
        defaultProduitShouldNotBeFound("factureId.equals=" + (factureId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProduitShouldBeFound(String filter) throws Exception {
        restProduitMockMvc.perform(get("/api/produits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(produit.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prixVenteHtva").value(hasItem(DEFAULT_PRIX_VENTE_HTVA.doubleValue())))
            .andExpect(jsonPath("$.[*].prixVenteTvac").value(hasItem(DEFAULT_PRIX_VENTE_TVAC.doubleValue())))
            .andExpect(jsonPath("$.[*].prixAchatHt").value(hasItem(DEFAULT_PRIX_ACHAT_HT.doubleValue())))
            .andExpect(jsonPath("$.[*].tauxTvaAchat").value(hasItem(DEFAULT_TAUX_TVA_ACHAT.doubleValue())))
            .andExpect(jsonPath("$.[*].tauxTva").value(hasItem(DEFAULT_TAUX_TVA.doubleValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProduitMockMvc.perform(get("/api/produits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProduitShouldNotBeFound(String filter) throws Exception {
        restProduitMockMvc.perform(get("/api/produits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProduitMockMvc.perform(get("/api/produits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProduit() throws Exception {
        // Get the produit
        restProduitMockMvc.perform(get("/api/produits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduit() throws Exception {
        // Initialize the database
        produitService.save(produit);

        int databaseSizeBeforeUpdate = produitRepository.findAll().size();

        // Update the produit
        Produit updatedProduit = produitRepository.findById(produit.getId()).get();
        // Disconnect from session so that the updates on updatedProduit are not directly saved in db
        em.detach(updatedProduit);
        updatedProduit
            .nom(UPDATED_NOM)
            .prixVenteHtva(UPDATED_PRIX_VENTE_HTVA)
            .prixVenteTvac(UPDATED_PRIX_VENTE_TVAC)
            .prixAchatHt(UPDATED_PRIX_ACHAT_HT)
            .tauxTvaAchat(UPDATED_TAUX_TVA_ACHAT)
            .tauxTva(UPDATED_TAUX_TVA)
            .observation(UPDATED_OBSERVATION)
            .description(UPDATED_DESCRIPTION);

        restProduitMockMvc.perform(put("/api/produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProduit)))
            .andExpect(status().isOk());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
        Produit testProduit = produitList.get(produitList.size() - 1);
        assertThat(testProduit.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testProduit.getPrixVenteHtva()).isEqualTo(UPDATED_PRIX_VENTE_HTVA);
        assertThat(testProduit.getPrixVenteTvac()).isEqualTo(UPDATED_PRIX_VENTE_TVAC);
        assertThat(testProduit.getPrixAchatHt()).isEqualTo(UPDATED_PRIX_ACHAT_HT);
        assertThat(testProduit.getTauxTvaAchat()).isEqualTo(UPDATED_TAUX_TVA_ACHAT);
        assertThat(testProduit.getTauxTva()).isEqualTo(UPDATED_TAUX_TVA);
        assertThat(testProduit.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingProduit() throws Exception {
        int databaseSizeBeforeUpdate = produitRepository.findAll().size();

        // Create the Produit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProduitMockMvc.perform(put("/api/produits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(produit)))
            .andExpect(status().isBadRequest());

        // Validate the Produit in the database
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProduit() throws Exception {
        // Initialize the database
        produitService.save(produit);

        int databaseSizeBeforeDelete = produitRepository.findAll().size();

        // Delete the produit
        restProduitMockMvc.perform(delete("/api/produits/{id}", produit.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Produit> produitList = produitRepository.findAll();
        assertThat(produitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
