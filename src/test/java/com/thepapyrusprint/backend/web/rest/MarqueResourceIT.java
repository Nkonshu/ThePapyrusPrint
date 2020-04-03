package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.ThePapyrusPrintApp;
import com.thepapyrusprint.backend.domain.Marque;
import com.thepapyrusprint.backend.domain.Produit;
import com.thepapyrusprint.backend.repository.MarqueRepository;
import com.thepapyrusprint.backend.service.MarqueService;
import com.thepapyrusprint.backend.service.dto.MarqueCriteria;
import com.thepapyrusprint.backend.service.MarqueQueryService;

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
 * Integration tests for the {@link MarqueResource} REST controller.
 */
@SpringBootTest(classes = ThePapyrusPrintApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class MarqueResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    @Autowired
    private MarqueRepository marqueRepository;

    @Autowired
    private MarqueService marqueService;

    @Autowired
    private MarqueQueryService marqueQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarqueMockMvc;

    private Marque marque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marque createEntity(EntityManager em) {
        Marque marque = new Marque()
            .nom(DEFAULT_NOM)
            .observation(DEFAULT_OBSERVATION);
        return marque;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marque createUpdatedEntity(EntityManager em) {
        Marque marque = new Marque()
            .nom(UPDATED_NOM)
            .observation(UPDATED_OBSERVATION);
        return marque;
    }

    @BeforeEach
    public void initTest() {
        marque = createEntity(em);
    }

    @Test
    @Transactional
    public void createMarque() throws Exception {
        int databaseSizeBeforeCreate = marqueRepository.findAll().size();

        // Create the Marque
        restMarqueMockMvc.perform(post("/api/marques")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(marque)))
            .andExpect(status().isCreated());

        // Validate the Marque in the database
        List<Marque> marqueList = marqueRepository.findAll();
        assertThat(marqueList).hasSize(databaseSizeBeforeCreate + 1);
        Marque testMarque = marqueList.get(marqueList.size() - 1);
        assertThat(testMarque.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testMarque.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    public void createMarqueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = marqueRepository.findAll().size();

        // Create the Marque with an existing ID
        marque.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarqueMockMvc.perform(post("/api/marques")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(marque)))
            .andExpect(status().isBadRequest());

        // Validate the Marque in the database
        List<Marque> marqueList = marqueRepository.findAll();
        assertThat(marqueList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = marqueRepository.findAll().size();
        // set the field null
        marque.setNom(null);

        // Create the Marque, which fails.

        restMarqueMockMvc.perform(post("/api/marques")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(marque)))
            .andExpect(status().isBadRequest());

        List<Marque> marqueList = marqueRepository.findAll();
        assertThat(marqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMarques() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList
        restMarqueMockMvc.perform(get("/api/marques?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marque.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }
    
    @Test
    @Transactional
    public void getMarque() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get the marque
        restMarqueMockMvc.perform(get("/api/marques/{id}", marque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marque.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }


    @Test
    @Transactional
    public void getMarquesByIdFiltering() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        Long id = marque.getId();

        defaultMarqueShouldBeFound("id.equals=" + id);
        defaultMarqueShouldNotBeFound("id.notEquals=" + id);

        defaultMarqueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMarqueShouldNotBeFound("id.greaterThan=" + id);

        defaultMarqueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMarqueShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMarquesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where nom equals to DEFAULT_NOM
        defaultMarqueShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the marqueList where nom equals to UPDATED_NOM
        defaultMarqueShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllMarquesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where nom not equals to DEFAULT_NOM
        defaultMarqueShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the marqueList where nom not equals to UPDATED_NOM
        defaultMarqueShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllMarquesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultMarqueShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the marqueList where nom equals to UPDATED_NOM
        defaultMarqueShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllMarquesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where nom is not null
        defaultMarqueShouldBeFound("nom.specified=true");

        // Get all the marqueList where nom is null
        defaultMarqueShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllMarquesByNomContainsSomething() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where nom contains DEFAULT_NOM
        defaultMarqueShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the marqueList where nom contains UPDATED_NOM
        defaultMarqueShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllMarquesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where nom does not contain DEFAULT_NOM
        defaultMarqueShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the marqueList where nom does not contain UPDATED_NOM
        defaultMarqueShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllMarquesByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where observation equals to DEFAULT_OBSERVATION
        defaultMarqueShouldBeFound("observation.equals=" + DEFAULT_OBSERVATION);

        // Get all the marqueList where observation equals to UPDATED_OBSERVATION
        defaultMarqueShouldNotBeFound("observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllMarquesByObservationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where observation not equals to DEFAULT_OBSERVATION
        defaultMarqueShouldNotBeFound("observation.notEquals=" + DEFAULT_OBSERVATION);

        // Get all the marqueList where observation not equals to UPDATED_OBSERVATION
        defaultMarqueShouldBeFound("observation.notEquals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllMarquesByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where observation in DEFAULT_OBSERVATION or UPDATED_OBSERVATION
        defaultMarqueShouldBeFound("observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION);

        // Get all the marqueList where observation equals to UPDATED_OBSERVATION
        defaultMarqueShouldNotBeFound("observation.in=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllMarquesByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where observation is not null
        defaultMarqueShouldBeFound("observation.specified=true");

        // Get all the marqueList where observation is null
        defaultMarqueShouldNotBeFound("observation.specified=false");
    }
                @Test
    @Transactional
    public void getAllMarquesByObservationContainsSomething() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where observation contains DEFAULT_OBSERVATION
        defaultMarqueShouldBeFound("observation.contains=" + DEFAULT_OBSERVATION);

        // Get all the marqueList where observation contains UPDATED_OBSERVATION
        defaultMarqueShouldNotBeFound("observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllMarquesByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);

        // Get all the marqueList where observation does not contain DEFAULT_OBSERVATION
        defaultMarqueShouldNotBeFound("observation.doesNotContain=" + DEFAULT_OBSERVATION);

        // Get all the marqueList where observation does not contain UPDATED_OBSERVATION
        defaultMarqueShouldBeFound("observation.doesNotContain=" + UPDATED_OBSERVATION);
    }


    @Test
    @Transactional
    public void getAllMarquesByProduitIsEqualToSomething() throws Exception {
        // Initialize the database
        marqueRepository.saveAndFlush(marque);
        Produit produit = ProduitResourceIT.createEntity(em);
        em.persist(produit);
        em.flush();
        marque.addProduit(produit);
        marqueRepository.saveAndFlush(marque);
        Long produitId = produit.getId();

        // Get all the marqueList where produit equals to produitId
        defaultMarqueShouldBeFound("produitId.equals=" + produitId);

        // Get all the marqueList where produit equals to produitId + 1
        defaultMarqueShouldNotBeFound("produitId.equals=" + (produitId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMarqueShouldBeFound(String filter) throws Exception {
        restMarqueMockMvc.perform(get("/api/marques?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marque.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));

        // Check, that the count call also returns 1
        restMarqueMockMvc.perform(get("/api/marques/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMarqueShouldNotBeFound(String filter) throws Exception {
        restMarqueMockMvc.perform(get("/api/marques?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMarqueMockMvc.perform(get("/api/marques/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMarque() throws Exception {
        // Get the marque
        restMarqueMockMvc.perform(get("/api/marques/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMarque() throws Exception {
        // Initialize the database
        marqueService.save(marque);

        int databaseSizeBeforeUpdate = marqueRepository.findAll().size();

        // Update the marque
        Marque updatedMarque = marqueRepository.findById(marque.getId()).get();
        // Disconnect from session so that the updates on updatedMarque are not directly saved in db
        em.detach(updatedMarque);
        updatedMarque
            .nom(UPDATED_NOM)
            .observation(UPDATED_OBSERVATION);

        restMarqueMockMvc.perform(put("/api/marques")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMarque)))
            .andExpect(status().isOk());

        // Validate the Marque in the database
        List<Marque> marqueList = marqueRepository.findAll();
        assertThat(marqueList).hasSize(databaseSizeBeforeUpdate);
        Marque testMarque = marqueList.get(marqueList.size() - 1);
        assertThat(testMarque.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMarque.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void updateNonExistingMarque() throws Exception {
        int databaseSizeBeforeUpdate = marqueRepository.findAll().size();

        // Create the Marque

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarqueMockMvc.perform(put("/api/marques")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(marque)))
            .andExpect(status().isBadRequest());

        // Validate the Marque in the database
        List<Marque> marqueList = marqueRepository.findAll();
        assertThat(marqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMarque() throws Exception {
        // Initialize the database
        marqueService.save(marque);

        int databaseSizeBeforeDelete = marqueRepository.findAll().size();

        // Delete the marque
        restMarqueMockMvc.perform(delete("/api/marques/{id}", marque.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Marque> marqueList = marqueRepository.findAll();
        assertThat(marqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
