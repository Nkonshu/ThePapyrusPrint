package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.ThePapyrusPrintApp;
import com.thepapyrusprint.backend.domain.Fournisseur;
import com.thepapyrusprint.backend.domain.Commande;
import com.thepapyrusprint.backend.domain.Catalogue;
import com.thepapyrusprint.backend.repository.FournisseurRepository;
import com.thepapyrusprint.backend.service.FournisseurService;
import com.thepapyrusprint.backend.service.dto.FournisseurCriteria;
import com.thepapyrusprint.backend.service.FournisseurQueryService;

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
 * Integration tests for the {@link FournisseurResource} REST controller.
 */
@SpringBootTest(classes = ThePapyrusPrintApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class FournisseurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "db)p@^=7<sg.'DxWK";
    private static final String UPDATED_EMAIL = "3|@b.3I";

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private FournisseurService fournisseurService;

    @Autowired
    private FournisseurQueryService fournisseurQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFournisseurMockMvc;

    private Fournisseur fournisseur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .nom(DEFAULT_NOM)
            .ville(DEFAULT_VILLE)
            .pays(DEFAULT_PAYS)
            .telephone(DEFAULT_TELEPHONE)
            .email(DEFAULT_EMAIL);
        return fournisseur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fournisseur createUpdatedEntity(EntityManager em) {
        Fournisseur fournisseur = new Fournisseur()
            .nom(UPDATED_NOM)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL);
        return fournisseur;
    }

    @BeforeEach
    public void initTest() {
        fournisseur = createEntity(em);
    }

    @Test
    @Transactional
    public void createFournisseur() throws Exception {
        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();

        // Create the Fournisseur
        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isCreated());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate + 1);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testFournisseur.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testFournisseur.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testFournisseur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testFournisseur.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createFournisseurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fournisseurRepository.findAll().size();

        // Create the Fournisseur with an existing ID
        fournisseur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setNom(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setVille(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setPays(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = fournisseurRepository.findAll().size();
        // set the field null
        fournisseur.setTelephone(null);

        // Create the Fournisseur, which fails.

        restFournisseurMockMvc.perform(post("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFournisseurs() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList
        restFournisseurMockMvc.perform(get("/api/fournisseurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }
    
    @Test
    @Transactional
    public void getFournisseur() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get the fournisseur
        restFournisseurMockMvc.perform(get("/api/fournisseurs/{id}", fournisseur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fournisseur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }


    @Test
    @Transactional
    public void getFournisseursByIdFiltering() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        Long id = fournisseur.getId();

        defaultFournisseurShouldBeFound("id.equals=" + id);
        defaultFournisseurShouldNotBeFound("id.notEquals=" + id);

        defaultFournisseurShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFournisseurShouldNotBeFound("id.greaterThan=" + id);

        defaultFournisseurShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFournisseurShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFournisseursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom equals to DEFAULT_NOM
        defaultFournisseurShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the fournisseurList where nom equals to UPDATED_NOM
        defaultFournisseurShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllFournisseursByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom not equals to DEFAULT_NOM
        defaultFournisseurShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the fournisseurList where nom not equals to UPDATED_NOM
        defaultFournisseurShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllFournisseursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultFournisseurShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the fournisseurList where nom equals to UPDATED_NOM
        defaultFournisseurShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllFournisseursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom is not null
        defaultFournisseurShouldBeFound("nom.specified=true");

        // Get all the fournisseurList where nom is null
        defaultFournisseurShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllFournisseursByNomContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom contains DEFAULT_NOM
        defaultFournisseurShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the fournisseurList where nom contains UPDATED_NOM
        defaultFournisseurShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllFournisseursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where nom does not contain DEFAULT_NOM
        defaultFournisseurShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the fournisseurList where nom does not contain UPDATED_NOM
        defaultFournisseurShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllFournisseursByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville equals to DEFAULT_VILLE
        defaultFournisseurShouldBeFound("ville.equals=" + DEFAULT_VILLE);

        // Get all the fournisseurList where ville equals to UPDATED_VILLE
        defaultFournisseurShouldNotBeFound("ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByVilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville not equals to DEFAULT_VILLE
        defaultFournisseurShouldNotBeFound("ville.notEquals=" + DEFAULT_VILLE);

        // Get all the fournisseurList where ville not equals to UPDATED_VILLE
        defaultFournisseurShouldBeFound("ville.notEquals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville in DEFAULT_VILLE or UPDATED_VILLE
        defaultFournisseurShouldBeFound("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE);

        // Get all the fournisseurList where ville equals to UPDATED_VILLE
        defaultFournisseurShouldNotBeFound("ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville is not null
        defaultFournisseurShouldBeFound("ville.specified=true");

        // Get all the fournisseurList where ville is null
        defaultFournisseurShouldNotBeFound("ville.specified=false");
    }
                @Test
    @Transactional
    public void getAllFournisseursByVilleContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville contains DEFAULT_VILLE
        defaultFournisseurShouldBeFound("ville.contains=" + DEFAULT_VILLE);

        // Get all the fournisseurList where ville contains UPDATED_VILLE
        defaultFournisseurShouldNotBeFound("ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where ville does not contain DEFAULT_VILLE
        defaultFournisseurShouldNotBeFound("ville.doesNotContain=" + DEFAULT_VILLE);

        // Get all the fournisseurList where ville does not contain UPDATED_VILLE
        defaultFournisseurShouldBeFound("ville.doesNotContain=" + UPDATED_VILLE);
    }


    @Test
    @Transactional
    public void getAllFournisseursByPaysIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where pays equals to DEFAULT_PAYS
        defaultFournisseurShouldBeFound("pays.equals=" + DEFAULT_PAYS);

        // Get all the fournisseurList where pays equals to UPDATED_PAYS
        defaultFournisseurShouldNotBeFound("pays.equals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllFournisseursByPaysIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where pays not equals to DEFAULT_PAYS
        defaultFournisseurShouldNotBeFound("pays.notEquals=" + DEFAULT_PAYS);

        // Get all the fournisseurList where pays not equals to UPDATED_PAYS
        defaultFournisseurShouldBeFound("pays.notEquals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllFournisseursByPaysIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where pays in DEFAULT_PAYS or UPDATED_PAYS
        defaultFournisseurShouldBeFound("pays.in=" + DEFAULT_PAYS + "," + UPDATED_PAYS);

        // Get all the fournisseurList where pays equals to UPDATED_PAYS
        defaultFournisseurShouldNotBeFound("pays.in=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllFournisseursByPaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where pays is not null
        defaultFournisseurShouldBeFound("pays.specified=true");

        // Get all the fournisseurList where pays is null
        defaultFournisseurShouldNotBeFound("pays.specified=false");
    }
                @Test
    @Transactional
    public void getAllFournisseursByPaysContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where pays contains DEFAULT_PAYS
        defaultFournisseurShouldBeFound("pays.contains=" + DEFAULT_PAYS);

        // Get all the fournisseurList where pays contains UPDATED_PAYS
        defaultFournisseurShouldNotBeFound("pays.contains=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllFournisseursByPaysNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where pays does not contain DEFAULT_PAYS
        defaultFournisseurShouldNotBeFound("pays.doesNotContain=" + DEFAULT_PAYS);

        // Get all the fournisseurList where pays does not contain UPDATED_PAYS
        defaultFournisseurShouldBeFound("pays.doesNotContain=" + UPDATED_PAYS);
    }


    @Test
    @Transactional
    public void getAllFournisseursByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where telephone equals to DEFAULT_TELEPHONE
        defaultFournisseurShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the fournisseurList where telephone equals to UPDATED_TELEPHONE
        defaultFournisseurShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where telephone not equals to DEFAULT_TELEPHONE
        defaultFournisseurShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the fournisseurList where telephone not equals to UPDATED_TELEPHONE
        defaultFournisseurShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultFournisseurShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the fournisseurList where telephone equals to UPDATED_TELEPHONE
        defaultFournisseurShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where telephone is not null
        defaultFournisseurShouldBeFound("telephone.specified=true");

        // Get all the fournisseurList where telephone is null
        defaultFournisseurShouldNotBeFound("telephone.specified=false");
    }
                @Test
    @Transactional
    public void getAllFournisseursByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where telephone contains DEFAULT_TELEPHONE
        defaultFournisseurShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the fournisseurList where telephone contains UPDATED_TELEPHONE
        defaultFournisseurShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllFournisseursByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where telephone does not contain DEFAULT_TELEPHONE
        defaultFournisseurShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the fournisseurList where telephone does not contain UPDATED_TELEPHONE
        defaultFournisseurShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }


    @Test
    @Transactional
    public void getAllFournisseursByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where email equals to DEFAULT_EMAIL
        defaultFournisseurShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the fournisseurList where email equals to UPDATED_EMAIL
        defaultFournisseurShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllFournisseursByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where email not equals to DEFAULT_EMAIL
        defaultFournisseurShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the fournisseurList where email not equals to UPDATED_EMAIL
        defaultFournisseurShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllFournisseursByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultFournisseurShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the fournisseurList where email equals to UPDATED_EMAIL
        defaultFournisseurShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllFournisseursByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where email is not null
        defaultFournisseurShouldBeFound("email.specified=true");

        // Get all the fournisseurList where email is null
        defaultFournisseurShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllFournisseursByEmailContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where email contains DEFAULT_EMAIL
        defaultFournisseurShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the fournisseurList where email contains UPDATED_EMAIL
        defaultFournisseurShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllFournisseursByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);

        // Get all the fournisseurList where email does not contain DEFAULT_EMAIL
        defaultFournisseurShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the fournisseurList where email does not contain UPDATED_EMAIL
        defaultFournisseurShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllFournisseursByCommandeIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);
        Commande commande = CommandeResourceIT.createEntity(em);
        em.persist(commande);
        em.flush();
        fournisseur.addCommande(commande);
        fournisseurRepository.saveAndFlush(fournisseur);
        Long commandeId = commande.getId();

        // Get all the fournisseurList where commande equals to commandeId
        defaultFournisseurShouldBeFound("commandeId.equals=" + commandeId);

        // Get all the fournisseurList where commande equals to commandeId + 1
        defaultFournisseurShouldNotBeFound("commandeId.equals=" + (commandeId + 1));
    }


    @Test
    @Transactional
    public void getAllFournisseursByCatalogueIsEqualToSomething() throws Exception {
        // Initialize the database
        fournisseurRepository.saveAndFlush(fournisseur);
        Catalogue catalogue = CatalogueResourceIT.createEntity(em);
        em.persist(catalogue);
        em.flush();
        fournisseur.addCatalogue(catalogue);
        fournisseurRepository.saveAndFlush(fournisseur);
        Long catalogueId = catalogue.getId();

        // Get all the fournisseurList where catalogue equals to catalogueId
        defaultFournisseurShouldBeFound("catalogueId.equals=" + catalogueId);

        // Get all the fournisseurList where catalogue equals to catalogueId + 1
        defaultFournisseurShouldNotBeFound("catalogueId.equals=" + (catalogueId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFournisseurShouldBeFound(String filter) throws Exception {
        restFournisseurMockMvc.perform(get("/api/fournisseurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fournisseur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restFournisseurMockMvc.perform(get("/api/fournisseurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFournisseurShouldNotBeFound(String filter) throws Exception {
        restFournisseurMockMvc.perform(get("/api/fournisseurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFournisseurMockMvc.perform(get("/api/fournisseurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFournisseur() throws Exception {
        // Get the fournisseur
        restFournisseurMockMvc.perform(get("/api/fournisseurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFournisseur() throws Exception {
        // Initialize the database
        fournisseurService.save(fournisseur);

        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Update the fournisseur
        Fournisseur updatedFournisseur = fournisseurRepository.findById(fournisseur.getId()).get();
        // Disconnect from session so that the updates on updatedFournisseur are not directly saved in db
        em.detach(updatedFournisseur);
        updatedFournisseur
            .nom(UPDATED_NOM)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL);

        restFournisseurMockMvc.perform(put("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFournisseur)))
            .andExpect(status().isOk());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
        Fournisseur testFournisseur = fournisseurList.get(fournisseurList.size() - 1);
        assertThat(testFournisseur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testFournisseur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testFournisseur.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testFournisseur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testFournisseur.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingFournisseur() throws Exception {
        int databaseSizeBeforeUpdate = fournisseurRepository.findAll().size();

        // Create the Fournisseur

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFournisseurMockMvc.perform(put("/api/fournisseurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fournisseur)))
            .andExpect(status().isBadRequest());

        // Validate the Fournisseur in the database
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFournisseur() throws Exception {
        // Initialize the database
        fournisseurService.save(fournisseur);

        int databaseSizeBeforeDelete = fournisseurRepository.findAll().size();

        // Delete the fournisseur
        restFournisseurMockMvc.perform(delete("/api/fournisseurs/{id}", fournisseur.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fournisseur> fournisseurList = fournisseurRepository.findAll();
        assertThat(fournisseurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
