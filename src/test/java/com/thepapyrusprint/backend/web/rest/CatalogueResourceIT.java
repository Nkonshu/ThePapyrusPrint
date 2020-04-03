package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.ThePapyrusPrintApp;
import com.thepapyrusprint.backend.domain.Catalogue;
import com.thepapyrusprint.backend.domain.Produit;
import com.thepapyrusprint.backend.domain.Fournisseur;
import com.thepapyrusprint.backend.repository.CatalogueRepository;
import com.thepapyrusprint.backend.service.CatalogueService;
import com.thepapyrusprint.backend.service.dto.CatalogueCriteria;
import com.thepapyrusprint.backend.service.CatalogueQueryService;

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
 * Integration tests for the {@link CatalogueResource} REST controller.
 */
@SpringBootTest(classes = ThePapyrusPrintApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CatalogueResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    @Autowired
    private CatalogueRepository catalogueRepository;

    @Mock
    private CatalogueRepository catalogueRepositoryMock;

    @Mock
    private CatalogueService catalogueServiceMock;

    @Autowired
    private CatalogueService catalogueService;

    @Autowired
    private CatalogueQueryService catalogueQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogueMockMvc;

    private Catalogue catalogue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogue createEntity(EntityManager em) {
        Catalogue catalogue = new Catalogue()
            .nom(DEFAULT_NOM)
            .observation(DEFAULT_OBSERVATION);
        return catalogue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalogue createUpdatedEntity(EntityManager em) {
        Catalogue catalogue = new Catalogue()
            .nom(UPDATED_NOM)
            .observation(UPDATED_OBSERVATION);
        return catalogue;
    }

    @BeforeEach
    public void initTest() {
        catalogue = createEntity(em);
    }

    @Test
    @Transactional
    public void createCatalogue() throws Exception {
        int databaseSizeBeforeCreate = catalogueRepository.findAll().size();

        // Create the Catalogue
        restCatalogueMockMvc.perform(post("/api/catalogues")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catalogue)))
            .andExpect(status().isCreated());

        // Validate the Catalogue in the database
        List<Catalogue> catalogueList = catalogueRepository.findAll();
        assertThat(catalogueList).hasSize(databaseSizeBeforeCreate + 1);
        Catalogue testCatalogue = catalogueList.get(catalogueList.size() - 1);
        assertThat(testCatalogue.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCatalogue.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    public void createCatalogueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = catalogueRepository.findAll().size();

        // Create the Catalogue with an existing ID
        catalogue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogueMockMvc.perform(post("/api/catalogues")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catalogue)))
            .andExpect(status().isBadRequest());

        // Validate the Catalogue in the database
        List<Catalogue> catalogueList = catalogueRepository.findAll();
        assertThat(catalogueList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogueRepository.findAll().size();
        // set the field null
        catalogue.setNom(null);

        // Create the Catalogue, which fails.

        restCatalogueMockMvc.perform(post("/api/catalogues")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catalogue)))
            .andExpect(status().isBadRequest());

        List<Catalogue> catalogueList = catalogueRepository.findAll();
        assertThat(catalogueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCatalogues() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList
        restCatalogueMockMvc.perform(get("/api/catalogues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogue.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCataloguesWithEagerRelationshipsIsEnabled() throws Exception {
        when(catalogueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCatalogueMockMvc.perform(get("/api/catalogues?eagerload=true"))
            .andExpect(status().isOk());

        verify(catalogueServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCataloguesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(catalogueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCatalogueMockMvc.perform(get("/api/catalogues?eagerload=true"))
            .andExpect(status().isOk());

        verify(catalogueServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCatalogue() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get the catalogue
        restCatalogueMockMvc.perform(get("/api/catalogues/{id}", catalogue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogue.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }


    @Test
    @Transactional
    public void getCataloguesByIdFiltering() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        Long id = catalogue.getId();

        defaultCatalogueShouldBeFound("id.equals=" + id);
        defaultCatalogueShouldNotBeFound("id.notEquals=" + id);

        defaultCatalogueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCatalogueShouldNotBeFound("id.greaterThan=" + id);

        defaultCatalogueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCatalogueShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCataloguesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where nom equals to DEFAULT_NOM
        defaultCatalogueShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the catalogueList where nom equals to UPDATED_NOM
        defaultCatalogueShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllCataloguesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where nom not equals to DEFAULT_NOM
        defaultCatalogueShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the catalogueList where nom not equals to UPDATED_NOM
        defaultCatalogueShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllCataloguesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultCatalogueShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the catalogueList where nom equals to UPDATED_NOM
        defaultCatalogueShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllCataloguesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where nom is not null
        defaultCatalogueShouldBeFound("nom.specified=true");

        // Get all the catalogueList where nom is null
        defaultCatalogueShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllCataloguesByNomContainsSomething() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where nom contains DEFAULT_NOM
        defaultCatalogueShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the catalogueList where nom contains UPDATED_NOM
        defaultCatalogueShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllCataloguesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where nom does not contain DEFAULT_NOM
        defaultCatalogueShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the catalogueList where nom does not contain UPDATED_NOM
        defaultCatalogueShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllCataloguesByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where observation equals to DEFAULT_OBSERVATION
        defaultCatalogueShouldBeFound("observation.equals=" + DEFAULT_OBSERVATION);

        // Get all the catalogueList where observation equals to UPDATED_OBSERVATION
        defaultCatalogueShouldNotBeFound("observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllCataloguesByObservationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where observation not equals to DEFAULT_OBSERVATION
        defaultCatalogueShouldNotBeFound("observation.notEquals=" + DEFAULT_OBSERVATION);

        // Get all the catalogueList where observation not equals to UPDATED_OBSERVATION
        defaultCatalogueShouldBeFound("observation.notEquals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllCataloguesByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where observation in DEFAULT_OBSERVATION or UPDATED_OBSERVATION
        defaultCatalogueShouldBeFound("observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION);

        // Get all the catalogueList where observation equals to UPDATED_OBSERVATION
        defaultCatalogueShouldNotBeFound("observation.in=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllCataloguesByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where observation is not null
        defaultCatalogueShouldBeFound("observation.specified=true");

        // Get all the catalogueList where observation is null
        defaultCatalogueShouldNotBeFound("observation.specified=false");
    }
                @Test
    @Transactional
    public void getAllCataloguesByObservationContainsSomething() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where observation contains DEFAULT_OBSERVATION
        defaultCatalogueShouldBeFound("observation.contains=" + DEFAULT_OBSERVATION);

        // Get all the catalogueList where observation contains UPDATED_OBSERVATION
        defaultCatalogueShouldNotBeFound("observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllCataloguesByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);

        // Get all the catalogueList where observation does not contain DEFAULT_OBSERVATION
        defaultCatalogueShouldNotBeFound("observation.doesNotContain=" + DEFAULT_OBSERVATION);

        // Get all the catalogueList where observation does not contain UPDATED_OBSERVATION
        defaultCatalogueShouldBeFound("observation.doesNotContain=" + UPDATED_OBSERVATION);
    }


    @Test
    @Transactional
    public void getAllCataloguesByProduitIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);
        Produit produit = ProduitResourceIT.createEntity(em);
        em.persist(produit);
        em.flush();
        catalogue.addProduit(produit);
        catalogueRepository.saveAndFlush(catalogue);
        Long produitId = produit.getId();

        // Get all the catalogueList where produit equals to produitId
        defaultCatalogueShouldBeFound("produitId.equals=" + produitId);

        // Get all the catalogueList where produit equals to produitId + 1
        defaultCatalogueShouldNotBeFound("produitId.equals=" + (produitId + 1));
    }


    @Test
    @Transactional
    public void getAllCataloguesByFournisseurIsEqualToSomething() throws Exception {
        // Initialize the database
        catalogueRepository.saveAndFlush(catalogue);
        Fournisseur fournisseur = FournisseurResourceIT.createEntity(em);
        em.persist(fournisseur);
        em.flush();
        catalogue.addFournisseur(fournisseur);
        catalogueRepository.saveAndFlush(catalogue);
        Long fournisseurId = fournisseur.getId();

        // Get all the catalogueList where fournisseur equals to fournisseurId
        defaultCatalogueShouldBeFound("fournisseurId.equals=" + fournisseurId);

        // Get all the catalogueList where fournisseur equals to fournisseurId + 1
        defaultCatalogueShouldNotBeFound("fournisseurId.equals=" + (fournisseurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCatalogueShouldBeFound(String filter) throws Exception {
        restCatalogueMockMvc.perform(get("/api/catalogues?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogue.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));

        // Check, that the count call also returns 1
        restCatalogueMockMvc.perform(get("/api/catalogues/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCatalogueShouldNotBeFound(String filter) throws Exception {
        restCatalogueMockMvc.perform(get("/api/catalogues?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCatalogueMockMvc.perform(get("/api/catalogues/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCatalogue() throws Exception {
        // Get the catalogue
        restCatalogueMockMvc.perform(get("/api/catalogues/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCatalogue() throws Exception {
        // Initialize the database
        catalogueService.save(catalogue);

        int databaseSizeBeforeUpdate = catalogueRepository.findAll().size();

        // Update the catalogue
        Catalogue updatedCatalogue = catalogueRepository.findById(catalogue.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogue are not directly saved in db
        em.detach(updatedCatalogue);
        updatedCatalogue
            .nom(UPDATED_NOM)
            .observation(UPDATED_OBSERVATION);

        restCatalogueMockMvc.perform(put("/api/catalogues")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCatalogue)))
            .andExpect(status().isOk());

        // Validate the Catalogue in the database
        List<Catalogue> catalogueList = catalogueRepository.findAll();
        assertThat(catalogueList).hasSize(databaseSizeBeforeUpdate);
        Catalogue testCatalogue = catalogueList.get(catalogueList.size() - 1);
        assertThat(testCatalogue.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCatalogue.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void updateNonExistingCatalogue() throws Exception {
        int databaseSizeBeforeUpdate = catalogueRepository.findAll().size();

        // Create the Catalogue

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogueMockMvc.perform(put("/api/catalogues")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(catalogue)))
            .andExpect(status().isBadRequest());

        // Validate the Catalogue in the database
        List<Catalogue> catalogueList = catalogueRepository.findAll();
        assertThat(catalogueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCatalogue() throws Exception {
        // Initialize the database
        catalogueService.save(catalogue);

        int databaseSizeBeforeDelete = catalogueRepository.findAll().size();

        // Delete the catalogue
        restCatalogueMockMvc.perform(delete("/api/catalogues/{id}", catalogue.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Catalogue> catalogueList = catalogueRepository.findAll();
        assertThat(catalogueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
