package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.ThePapyrusPrintApp;
import com.thepapyrusprint.backend.domain.Commande;
import com.thepapyrusprint.backend.domain.Produit;
import com.thepapyrusprint.backend.domain.Fournisseur;
import com.thepapyrusprint.backend.repository.CommandeRepository;
import com.thepapyrusprint.backend.service.CommandeService;
import com.thepapyrusprint.backend.service.dto.CommandeCriteria;
import com.thepapyrusprint.backend.service.CommandeQueryService;

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

/**
 * Integration tests for the {@link CommandeResource} REST controller.
 */
@SpringBootTest(classes = ThePapyrusPrintApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CommandeResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    @Autowired
    private CommandeRepository commandeRepository;

    @Mock
    private CommandeRepository commandeRepositoryMock;

    @Mock
    private CommandeService commandeServiceMock;

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private CommandeQueryService commandeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommandeMockMvc;

    private Commande commande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createEntity(EntityManager em) {
        Commande commande = new Commande()
            .date(DEFAULT_DATE)
            .observation(DEFAULT_OBSERVATION);
        return commande;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createUpdatedEntity(EntityManager em) {
        Commande commande = new Commande()
            .date(UPDATED_DATE)
            .observation(UPDATED_OBSERVATION);
        return commande;
    }

    @BeforeEach
    public void initTest() {
        commande = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // Create the Commande
        restCommandeMockMvc.perform(post("/api/commandes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isCreated());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCommande.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    public void createCommandeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // Create the Commande with an existing ID
        commande.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandeMockMvc.perform(post("/api/commandes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCommandes() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList
        restCommandeMockMvc.perform(get("/api/commandes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCommandesWithEagerRelationshipsIsEnabled() throws Exception {
        when(commandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommandeMockMvc.perform(get("/api/commandes?eagerload=true"))
            .andExpect(status().isOk());

        verify(commandeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCommandesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(commandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCommandeMockMvc.perform(get("/api/commandes?eagerload=true"))
            .andExpect(status().isOk());

        verify(commandeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commande.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }


    @Test
    @Transactional
    public void getCommandesByIdFiltering() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        Long id = commande.getId();

        defaultCommandeShouldBeFound("id.equals=" + id);
        defaultCommandeShouldNotBeFound("id.notEquals=" + id);

        defaultCommandeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommandeShouldNotBeFound("id.greaterThan=" + id);

        defaultCommandeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommandeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCommandesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date equals to DEFAULT_DATE
        defaultCommandeShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the commandeList where date equals to UPDATED_DATE
        defaultCommandeShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date not equals to DEFAULT_DATE
        defaultCommandeShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the commandeList where date not equals to UPDATED_DATE
        defaultCommandeShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date in DEFAULT_DATE or UPDATED_DATE
        defaultCommandeShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the commandeList where date equals to UPDATED_DATE
        defaultCommandeShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is not null
        defaultCommandeShouldBeFound("date.specified=true");

        // Get all the commandeList where date is null
        defaultCommandeShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is greater than or equal to DEFAULT_DATE
        defaultCommandeShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the commandeList where date is greater than or equal to UPDATED_DATE
        defaultCommandeShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is less than or equal to DEFAULT_DATE
        defaultCommandeShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the commandeList where date is less than or equal to SMALLER_DATE
        defaultCommandeShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is less than DEFAULT_DATE
        defaultCommandeShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the commandeList where date is less than UPDATED_DATE
        defaultCommandeShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is greater than DEFAULT_DATE
        defaultCommandeShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the commandeList where date is greater than SMALLER_DATE
        defaultCommandeShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllCommandesByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where observation equals to DEFAULT_OBSERVATION
        defaultCommandeShouldBeFound("observation.equals=" + DEFAULT_OBSERVATION);

        // Get all the commandeList where observation equals to UPDATED_OBSERVATION
        defaultCommandeShouldNotBeFound("observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllCommandesByObservationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where observation not equals to DEFAULT_OBSERVATION
        defaultCommandeShouldNotBeFound("observation.notEquals=" + DEFAULT_OBSERVATION);

        // Get all the commandeList where observation not equals to UPDATED_OBSERVATION
        defaultCommandeShouldBeFound("observation.notEquals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllCommandesByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where observation in DEFAULT_OBSERVATION or UPDATED_OBSERVATION
        defaultCommandeShouldBeFound("observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION);

        // Get all the commandeList where observation equals to UPDATED_OBSERVATION
        defaultCommandeShouldNotBeFound("observation.in=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllCommandesByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where observation is not null
        defaultCommandeShouldBeFound("observation.specified=true");

        // Get all the commandeList where observation is null
        defaultCommandeShouldNotBeFound("observation.specified=false");
    }
                @Test
    @Transactional
    public void getAllCommandesByObservationContainsSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where observation contains DEFAULT_OBSERVATION
        defaultCommandeShouldBeFound("observation.contains=" + DEFAULT_OBSERVATION);

        // Get all the commandeList where observation contains UPDATED_OBSERVATION
        defaultCommandeShouldNotBeFound("observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllCommandesByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where observation does not contain DEFAULT_OBSERVATION
        defaultCommandeShouldNotBeFound("observation.doesNotContain=" + DEFAULT_OBSERVATION);

        // Get all the commandeList where observation does not contain UPDATED_OBSERVATION
        defaultCommandeShouldBeFound("observation.doesNotContain=" + UPDATED_OBSERVATION);
    }


    @Test
    @Transactional
    public void getAllCommandesByProduitIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        Produit produit = ProduitResourceIT.createEntity(em);
        em.persist(produit);
        em.flush();
        commande.addProduit(produit);
        commandeRepository.saveAndFlush(commande);
        Long produitId = produit.getId();

        // Get all the commandeList where produit equals to produitId
        defaultCommandeShouldBeFound("produitId.equals=" + produitId);

        // Get all the commandeList where produit equals to produitId + 1
        defaultCommandeShouldNotBeFound("produitId.equals=" + (produitId + 1));
    }


    @Test
    @Transactional
    public void getAllCommandesByFournisseurIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        Fournisseur fournisseur = FournisseurResourceIT.createEntity(em);
        em.persist(fournisseur);
        em.flush();
        commande.setFournisseur(fournisseur);
        commandeRepository.saveAndFlush(commande);
        Long fournisseurId = fournisseur.getId();

        // Get all the commandeList where fournisseur equals to fournisseurId
        defaultCommandeShouldBeFound("fournisseurId.equals=" + fournisseurId);

        // Get all the commandeList where fournisseur equals to fournisseurId + 1
        defaultCommandeShouldNotBeFound("fournisseurId.equals=" + (fournisseurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommandeShouldBeFound(String filter) throws Exception {
        restCommandeMockMvc.perform(get("/api/commandes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));

        // Check, that the count call also returns 1
        restCommandeMockMvc.perform(get("/api/commandes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommandeShouldNotBeFound(String filter) throws Exception {
        restCommandeMockMvc.perform(get("/api/commandes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommandeMockMvc.perform(get("/api/commandes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCommande() throws Exception {
        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommande() throws Exception {
        // Initialize the database
        commandeService.save(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande
        Commande updatedCommande = commandeRepository.findById(commande.getId()).get();
        // Disconnect from session so that the updates on updatedCommande are not directly saved in db
        em.detach(updatedCommande);
        updatedCommande
            .date(UPDATED_DATE)
            .observation(UPDATED_OBSERVATION);

        restCommandeMockMvc.perform(put("/api/commandes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommande)))
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCommande.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void updateNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Create the Commande

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeMockMvc.perform(put("/api/commandes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(commande)))
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommande() throws Exception {
        // Initialize the database
        commandeService.save(commande);

        int databaseSizeBeforeDelete = commandeRepository.findAll().size();

        // Delete the commande
        restCommandeMockMvc.perform(delete("/api/commandes/{id}", commande.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
