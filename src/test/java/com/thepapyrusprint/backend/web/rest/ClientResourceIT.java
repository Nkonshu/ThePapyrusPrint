package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.ThePapyrusPrintApp;
import com.thepapyrusprint.backend.domain.Client;
import com.thepapyrusprint.backend.domain.User;
import com.thepapyrusprint.backend.domain.Client;
import com.thepapyrusprint.backend.domain.Facture;
import com.thepapyrusprint.backend.repository.ClientRepository;
import com.thepapyrusprint.backend.service.ClientService;
import com.thepapyrusprint.backend.service.dto.ClientCriteria;
import com.thepapyrusprint.backend.service.ClientQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.thepapyrusprint.backend.domain.enumeration.Civilite;
import com.thepapyrusprint.backend.domain.enumeration.Sexe;
/**
 * Integration tests for the {@link ClientResource} REST controller.
 */
@SpringBootTest(classes = ThePapyrusPrintApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ClientResourceIT {

    private static final Civilite DEFAULT_CIVILITE = Civilite.Mr;
    private static final Civilite UPDATED_CIVILITE = Civilite.Mm;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.M;
    private static final Sexe UPDATED_SEXE = Sexe.F;

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_NAISSANCE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_QUARTIER = "AAAAAAAAAA";
    private static final String UPDATED_QUARTIER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "A+[@1pcoc.lu1";
    private static final String UPDATED_EMAIL = "t8o@DsWE1$.z!8M>x";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientQueryService clientQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
            .civilite(DEFAULT_CIVILITE)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .sexe(DEFAULT_SEXE)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .telephone(DEFAULT_TELEPHONE)
            .ville(DEFAULT_VILLE)
            .codePostal(DEFAULT_CODE_POSTAL)
            .pays(DEFAULT_PAYS)
            .quartier(DEFAULT_QUARTIER)
            .email(DEFAULT_EMAIL)
            .observation(DEFAULT_OBSERVATION);
        return client;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity(EntityManager em) {
        Client client = new Client()
            .civilite(UPDATED_CIVILITE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .sexe(UPDATED_SEXE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .telephone(UPDATED_TELEPHONE)
            .ville(UPDATED_VILLE)
            .codePostal(UPDATED_CODE_POSTAL)
            .pays(UPDATED_PAYS)
            .quartier(UPDATED_QUARTIER)
            .email(UPDATED_EMAIL)
            .observation(UPDATED_OBSERVATION);
        return client;
    }

    @BeforeEach
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client
        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getCivilite()).isEqualTo(DEFAULT_CIVILITE);
        assertThat(testClient.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testClient.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testClient.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testClient.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testClient.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testClient.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testClient.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testClient.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testClient.getQuartier()).isEqualTo(DEFAULT_QUARTIER);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    public void createClientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client with an existing ID
        client.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCiviliteIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setCivilite(null);

        // Create the Client, which fails.

        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setNom(null);

        // Create the Client, which fails.

        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setPrenom(null);

        // Create the Client, which fails.

        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setSexe(null);

        // Create the Client, which fails.

        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateNaissanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setDateNaissance(null);

        // Create the Client, which fails.

        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setTelephone(null);

        // Create the Client, which fails.

        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setVille(null);

        // Create the Client, which fails.

        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setPays(null);

        // Create the Client, which fails.

        restClientMockMvc.perform(post("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc.perform(get("/api/clients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].civilite").value(hasItem(DEFAULT_CIVILITE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].quartier").value(hasItem(DEFAULT_QUARTIER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }
    
    @Test
    @Transactional
    public void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.civilite").value(DEFAULT_CIVILITE.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.quartier").value(DEFAULT_QUARTIER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }


    @Test
    @Transactional
    public void getClientsByIdFiltering() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        Long id = client.getId();

        defaultClientShouldBeFound("id.equals=" + id);
        defaultClientShouldNotBeFound("id.notEquals=" + id);

        defaultClientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClientShouldNotBeFound("id.greaterThan=" + id);

        defaultClientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClientShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllClientsByCiviliteIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where civilite equals to DEFAULT_CIVILITE
        defaultClientShouldBeFound("civilite.equals=" + DEFAULT_CIVILITE);

        // Get all the clientList where civilite equals to UPDATED_CIVILITE
        defaultClientShouldNotBeFound("civilite.equals=" + UPDATED_CIVILITE);
    }

    @Test
    @Transactional
    public void getAllClientsByCiviliteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where civilite not equals to DEFAULT_CIVILITE
        defaultClientShouldNotBeFound("civilite.notEquals=" + DEFAULT_CIVILITE);

        // Get all the clientList where civilite not equals to UPDATED_CIVILITE
        defaultClientShouldBeFound("civilite.notEquals=" + UPDATED_CIVILITE);
    }

    @Test
    @Transactional
    public void getAllClientsByCiviliteIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where civilite in DEFAULT_CIVILITE or UPDATED_CIVILITE
        defaultClientShouldBeFound("civilite.in=" + DEFAULT_CIVILITE + "," + UPDATED_CIVILITE);

        // Get all the clientList where civilite equals to UPDATED_CIVILITE
        defaultClientShouldNotBeFound("civilite.in=" + UPDATED_CIVILITE);
    }

    @Test
    @Transactional
    public void getAllClientsByCiviliteIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where civilite is not null
        defaultClientShouldBeFound("civilite.specified=true");

        // Get all the clientList where civilite is null
        defaultClientShouldNotBeFound("civilite.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nom equals to DEFAULT_NOM
        defaultClientShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the clientList where nom equals to UPDATED_NOM
        defaultClientShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllClientsByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nom not equals to DEFAULT_NOM
        defaultClientShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the clientList where nom not equals to UPDATED_NOM
        defaultClientShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllClientsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultClientShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the clientList where nom equals to UPDATED_NOM
        defaultClientShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllClientsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nom is not null
        defaultClientShouldBeFound("nom.specified=true");

        // Get all the clientList where nom is null
        defaultClientShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByNomContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nom contains DEFAULT_NOM
        defaultClientShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the clientList where nom contains UPDATED_NOM
        defaultClientShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllClientsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where nom does not contain DEFAULT_NOM
        defaultClientShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the clientList where nom does not contain UPDATED_NOM
        defaultClientShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllClientsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom equals to DEFAULT_PRENOM
        defaultClientShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the clientList where prenom equals to UPDATED_PRENOM
        defaultClientShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllClientsByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom not equals to DEFAULT_PRENOM
        defaultClientShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the clientList where prenom not equals to UPDATED_PRENOM
        defaultClientShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllClientsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultClientShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the clientList where prenom equals to UPDATED_PRENOM
        defaultClientShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllClientsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom is not null
        defaultClientShouldBeFound("prenom.specified=true");

        // Get all the clientList where prenom is null
        defaultClientShouldNotBeFound("prenom.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom contains DEFAULT_PRENOM
        defaultClientShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the clientList where prenom contains UPDATED_PRENOM
        defaultClientShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllClientsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where prenom does not contain DEFAULT_PRENOM
        defaultClientShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the clientList where prenom does not contain UPDATED_PRENOM
        defaultClientShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }


    @Test
    @Transactional
    public void getAllClientsBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where sexe equals to DEFAULT_SEXE
        defaultClientShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the clientList where sexe equals to UPDATED_SEXE
        defaultClientShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllClientsBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where sexe not equals to DEFAULT_SEXE
        defaultClientShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the clientList where sexe not equals to UPDATED_SEXE
        defaultClientShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllClientsBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultClientShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the clientList where sexe equals to UPDATED_SEXE
        defaultClientShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllClientsBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where sexe is not null
        defaultClientShouldBeFound("sexe.specified=true");

        // Get all the clientList where sexe is null
        defaultClientShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where dateNaissance equals to DEFAULT_DATE_NAISSANCE
        defaultClientShouldBeFound("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the clientList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultClientShouldNotBeFound("dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllClientsByDateNaissanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where dateNaissance not equals to DEFAULT_DATE_NAISSANCE
        defaultClientShouldNotBeFound("dateNaissance.notEquals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the clientList where dateNaissance not equals to UPDATED_DATE_NAISSANCE
        defaultClientShouldBeFound("dateNaissance.notEquals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllClientsByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where dateNaissance in DEFAULT_DATE_NAISSANCE or UPDATED_DATE_NAISSANCE
        defaultClientShouldBeFound("dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE);

        // Get all the clientList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultClientShouldNotBeFound("dateNaissance.in=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllClientsByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where dateNaissance is not null
        defaultClientShouldBeFound("dateNaissance.specified=true");

        // Get all the clientList where dateNaissance is null
        defaultClientShouldNotBeFound("dateNaissance.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByDateNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where dateNaissance is greater than or equal to DEFAULT_DATE_NAISSANCE
        defaultClientShouldBeFound("dateNaissance.greaterThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the clientList where dateNaissance is greater than or equal to UPDATED_DATE_NAISSANCE
        defaultClientShouldNotBeFound("dateNaissance.greaterThanOrEqual=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllClientsByDateNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where dateNaissance is less than or equal to DEFAULT_DATE_NAISSANCE
        defaultClientShouldBeFound("dateNaissance.lessThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the clientList where dateNaissance is less than or equal to SMALLER_DATE_NAISSANCE
        defaultClientShouldNotBeFound("dateNaissance.lessThanOrEqual=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllClientsByDateNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where dateNaissance is less than DEFAULT_DATE_NAISSANCE
        defaultClientShouldNotBeFound("dateNaissance.lessThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the clientList where dateNaissance is less than UPDATED_DATE_NAISSANCE
        defaultClientShouldBeFound("dateNaissance.lessThan=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void getAllClientsByDateNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where dateNaissance is greater than DEFAULT_DATE_NAISSANCE
        defaultClientShouldNotBeFound("dateNaissance.greaterThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the clientList where dateNaissance is greater than SMALLER_DATE_NAISSANCE
        defaultClientShouldBeFound("dateNaissance.greaterThan=" + SMALLER_DATE_NAISSANCE);
    }


    @Test
    @Transactional
    public void getAllClientsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where telephone equals to DEFAULT_TELEPHONE
        defaultClientShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the clientList where telephone equals to UPDATED_TELEPHONE
        defaultClientShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllClientsByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where telephone not equals to DEFAULT_TELEPHONE
        defaultClientShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the clientList where telephone not equals to UPDATED_TELEPHONE
        defaultClientShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllClientsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultClientShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the clientList where telephone equals to UPDATED_TELEPHONE
        defaultClientShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllClientsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where telephone is not null
        defaultClientShouldBeFound("telephone.specified=true");

        // Get all the clientList where telephone is null
        defaultClientShouldNotBeFound("telephone.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where telephone contains DEFAULT_TELEPHONE
        defaultClientShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the clientList where telephone contains UPDATED_TELEPHONE
        defaultClientShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllClientsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where telephone does not contain DEFAULT_TELEPHONE
        defaultClientShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the clientList where telephone does not contain UPDATED_TELEPHONE
        defaultClientShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }


    @Test
    @Transactional
    public void getAllClientsByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where ville equals to DEFAULT_VILLE
        defaultClientShouldBeFound("ville.equals=" + DEFAULT_VILLE);

        // Get all the clientList where ville equals to UPDATED_VILLE
        defaultClientShouldNotBeFound("ville.equals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllClientsByVilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where ville not equals to DEFAULT_VILLE
        defaultClientShouldNotBeFound("ville.notEquals=" + DEFAULT_VILLE);

        // Get all the clientList where ville not equals to UPDATED_VILLE
        defaultClientShouldBeFound("ville.notEquals=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllClientsByVilleIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where ville in DEFAULT_VILLE or UPDATED_VILLE
        defaultClientShouldBeFound("ville.in=" + DEFAULT_VILLE + "," + UPDATED_VILLE);

        // Get all the clientList where ville equals to UPDATED_VILLE
        defaultClientShouldNotBeFound("ville.in=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllClientsByVilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where ville is not null
        defaultClientShouldBeFound("ville.specified=true");

        // Get all the clientList where ville is null
        defaultClientShouldNotBeFound("ville.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByVilleContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where ville contains DEFAULT_VILLE
        defaultClientShouldBeFound("ville.contains=" + DEFAULT_VILLE);

        // Get all the clientList where ville contains UPDATED_VILLE
        defaultClientShouldNotBeFound("ville.contains=" + UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void getAllClientsByVilleNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where ville does not contain DEFAULT_VILLE
        defaultClientShouldNotBeFound("ville.doesNotContain=" + DEFAULT_VILLE);

        // Get all the clientList where ville does not contain UPDATED_VILLE
        defaultClientShouldBeFound("ville.doesNotContain=" + UPDATED_VILLE);
    }


    @Test
    @Transactional
    public void getAllClientsByCodePostalIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal equals to DEFAULT_CODE_POSTAL
        defaultClientShouldBeFound("codePostal.equals=" + DEFAULT_CODE_POSTAL);

        // Get all the clientList where codePostal equals to UPDATED_CODE_POSTAL
        defaultClientShouldNotBeFound("codePostal.equals=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllClientsByCodePostalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal not equals to DEFAULT_CODE_POSTAL
        defaultClientShouldNotBeFound("codePostal.notEquals=" + DEFAULT_CODE_POSTAL);

        // Get all the clientList where codePostal not equals to UPDATED_CODE_POSTAL
        defaultClientShouldBeFound("codePostal.notEquals=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllClientsByCodePostalIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal in DEFAULT_CODE_POSTAL or UPDATED_CODE_POSTAL
        defaultClientShouldBeFound("codePostal.in=" + DEFAULT_CODE_POSTAL + "," + UPDATED_CODE_POSTAL);

        // Get all the clientList where codePostal equals to UPDATED_CODE_POSTAL
        defaultClientShouldNotBeFound("codePostal.in=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllClientsByCodePostalIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal is not null
        defaultClientShouldBeFound("codePostal.specified=true");

        // Get all the clientList where codePostal is null
        defaultClientShouldNotBeFound("codePostal.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByCodePostalContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal contains DEFAULT_CODE_POSTAL
        defaultClientShouldBeFound("codePostal.contains=" + DEFAULT_CODE_POSTAL);

        // Get all the clientList where codePostal contains UPDATED_CODE_POSTAL
        defaultClientShouldNotBeFound("codePostal.contains=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    public void getAllClientsByCodePostalNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where codePostal does not contain DEFAULT_CODE_POSTAL
        defaultClientShouldNotBeFound("codePostal.doesNotContain=" + DEFAULT_CODE_POSTAL);

        // Get all the clientList where codePostal does not contain UPDATED_CODE_POSTAL
        defaultClientShouldBeFound("codePostal.doesNotContain=" + UPDATED_CODE_POSTAL);
    }


    @Test
    @Transactional
    public void getAllClientsByPaysIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where pays equals to DEFAULT_PAYS
        defaultClientShouldBeFound("pays.equals=" + DEFAULT_PAYS);

        // Get all the clientList where pays equals to UPDATED_PAYS
        defaultClientShouldNotBeFound("pays.equals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllClientsByPaysIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where pays not equals to DEFAULT_PAYS
        defaultClientShouldNotBeFound("pays.notEquals=" + DEFAULT_PAYS);

        // Get all the clientList where pays not equals to UPDATED_PAYS
        defaultClientShouldBeFound("pays.notEquals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllClientsByPaysIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where pays in DEFAULT_PAYS or UPDATED_PAYS
        defaultClientShouldBeFound("pays.in=" + DEFAULT_PAYS + "," + UPDATED_PAYS);

        // Get all the clientList where pays equals to UPDATED_PAYS
        defaultClientShouldNotBeFound("pays.in=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllClientsByPaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where pays is not null
        defaultClientShouldBeFound("pays.specified=true");

        // Get all the clientList where pays is null
        defaultClientShouldNotBeFound("pays.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByPaysContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where pays contains DEFAULT_PAYS
        defaultClientShouldBeFound("pays.contains=" + DEFAULT_PAYS);

        // Get all the clientList where pays contains UPDATED_PAYS
        defaultClientShouldNotBeFound("pays.contains=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    public void getAllClientsByPaysNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where pays does not contain DEFAULT_PAYS
        defaultClientShouldNotBeFound("pays.doesNotContain=" + DEFAULT_PAYS);

        // Get all the clientList where pays does not contain UPDATED_PAYS
        defaultClientShouldBeFound("pays.doesNotContain=" + UPDATED_PAYS);
    }


    @Test
    @Transactional
    public void getAllClientsByQuartierIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where quartier equals to DEFAULT_QUARTIER
        defaultClientShouldBeFound("quartier.equals=" + DEFAULT_QUARTIER);

        // Get all the clientList where quartier equals to UPDATED_QUARTIER
        defaultClientShouldNotBeFound("quartier.equals=" + UPDATED_QUARTIER);
    }

    @Test
    @Transactional
    public void getAllClientsByQuartierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where quartier not equals to DEFAULT_QUARTIER
        defaultClientShouldNotBeFound("quartier.notEquals=" + DEFAULT_QUARTIER);

        // Get all the clientList where quartier not equals to UPDATED_QUARTIER
        defaultClientShouldBeFound("quartier.notEquals=" + UPDATED_QUARTIER);
    }

    @Test
    @Transactional
    public void getAllClientsByQuartierIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where quartier in DEFAULT_QUARTIER or UPDATED_QUARTIER
        defaultClientShouldBeFound("quartier.in=" + DEFAULT_QUARTIER + "," + UPDATED_QUARTIER);

        // Get all the clientList where quartier equals to UPDATED_QUARTIER
        defaultClientShouldNotBeFound("quartier.in=" + UPDATED_QUARTIER);
    }

    @Test
    @Transactional
    public void getAllClientsByQuartierIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where quartier is not null
        defaultClientShouldBeFound("quartier.specified=true");

        // Get all the clientList where quartier is null
        defaultClientShouldNotBeFound("quartier.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByQuartierContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where quartier contains DEFAULT_QUARTIER
        defaultClientShouldBeFound("quartier.contains=" + DEFAULT_QUARTIER);

        // Get all the clientList where quartier contains UPDATED_QUARTIER
        defaultClientShouldNotBeFound("quartier.contains=" + UPDATED_QUARTIER);
    }

    @Test
    @Transactional
    public void getAllClientsByQuartierNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where quartier does not contain DEFAULT_QUARTIER
        defaultClientShouldNotBeFound("quartier.doesNotContain=" + DEFAULT_QUARTIER);

        // Get all the clientList where quartier does not contain UPDATED_QUARTIER
        defaultClientShouldBeFound("quartier.doesNotContain=" + UPDATED_QUARTIER);
    }


    @Test
    @Transactional
    public void getAllClientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email equals to DEFAULT_EMAIL
        defaultClientShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the clientList where email equals to UPDATED_EMAIL
        defaultClientShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllClientsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email not equals to DEFAULT_EMAIL
        defaultClientShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the clientList where email not equals to UPDATED_EMAIL
        defaultClientShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllClientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultClientShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the clientList where email equals to UPDATED_EMAIL
        defaultClientShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllClientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email is not null
        defaultClientShouldBeFound("email.specified=true");

        // Get all the clientList where email is null
        defaultClientShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByEmailContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email contains DEFAULT_EMAIL
        defaultClientShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the clientList where email contains UPDATED_EMAIL
        defaultClientShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllClientsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email does not contain DEFAULT_EMAIL
        defaultClientShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the clientList where email does not contain UPDATED_EMAIL
        defaultClientShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllClientsByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where observation equals to DEFAULT_OBSERVATION
        defaultClientShouldBeFound("observation.equals=" + DEFAULT_OBSERVATION);

        // Get all the clientList where observation equals to UPDATED_OBSERVATION
        defaultClientShouldNotBeFound("observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllClientsByObservationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where observation not equals to DEFAULT_OBSERVATION
        defaultClientShouldNotBeFound("observation.notEquals=" + DEFAULT_OBSERVATION);

        // Get all the clientList where observation not equals to UPDATED_OBSERVATION
        defaultClientShouldBeFound("observation.notEquals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllClientsByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where observation in DEFAULT_OBSERVATION or UPDATED_OBSERVATION
        defaultClientShouldBeFound("observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION);

        // Get all the clientList where observation equals to UPDATED_OBSERVATION
        defaultClientShouldNotBeFound("observation.in=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllClientsByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where observation is not null
        defaultClientShouldBeFound("observation.specified=true");

        // Get all the clientList where observation is null
        defaultClientShouldNotBeFound("observation.specified=false");
    }
                @Test
    @Transactional
    public void getAllClientsByObservationContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where observation contains DEFAULT_OBSERVATION
        defaultClientShouldBeFound("observation.contains=" + DEFAULT_OBSERVATION);

        // Get all the clientList where observation contains UPDATED_OBSERVATION
        defaultClientShouldNotBeFound("observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void getAllClientsByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where observation does not contain DEFAULT_OBSERVATION
        defaultClientShouldNotBeFound("observation.doesNotContain=" + DEFAULT_OBSERVATION);

        // Get all the clientList where observation does not contain UPDATED_OBSERVATION
        defaultClientShouldBeFound("observation.doesNotContain=" + UPDATED_OBSERVATION);
    }


    @Test
    @Transactional
    public void getAllClientsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        client.setUser(user);
        clientRepository.saveAndFlush(client);
        Long userId = user.getId();

        // Get all the clientList where user equals to userId
        defaultClientShouldBeFound("userId.equals=" + userId);

        // Get all the clientList where user equals to userId + 1
        defaultClientShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllClientsByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        Client image = ClientResourceIT.createEntity(em);
        em.persist(image);
        em.flush();
        client.setImage(image);
        clientRepository.saveAndFlush(client);
        Long imageId = image.getId();

        // Get all the clientList where image equals to imageId
        defaultClientShouldBeFound("imageId.equals=" + imageId);

        // Get all the clientList where image equals to imageId + 1
        defaultClientShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }


    @Test
    @Transactional
    public void getAllClientsByFactureIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        Facture facture = FactureResourceIT.createEntity(em);
        em.persist(facture);
        em.flush();
        client.addFacture(facture);
        clientRepository.saveAndFlush(client);
        Long factureId = facture.getId();

        // Get all the clientList where facture equals to factureId
        defaultClientShouldBeFound("factureId.equals=" + factureId);

        // Get all the clientList where facture equals to factureId + 1
        defaultClientShouldNotBeFound("factureId.equals=" + (factureId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc.perform(get("/api/clients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].civilite").value(hasItem(DEFAULT_CIVILITE.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].quartier").value(hasItem(DEFAULT_QUARTIER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));

        // Check, that the count call also returns 1
        restClientMockMvc.perform(get("/api/clients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc.perform(get("/api/clients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc.perform(get("/api/clients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClient() throws Exception {
        // Initialize the database
        clientService.save(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).get();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .civilite(UPDATED_CIVILITE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .sexe(UPDATED_SEXE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .telephone(UPDATED_TELEPHONE)
            .ville(UPDATED_VILLE)
            .codePostal(UPDATED_CODE_POSTAL)
            .pays(UPDATED_PAYS)
            .quartier(UPDATED_QUARTIER)
            .email(UPDATED_EMAIL)
            .observation(UPDATED_OBSERVATION);

        restClientMockMvc.perform(put("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedClient)))
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getCivilite()).isEqualTo(UPDATED_CIVILITE);
        assertThat(testClient.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testClient.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testClient.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testClient.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testClient.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testClient.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testClient.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testClient.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testClient.getQuartier()).isEqualTo(UPDATED_QUARTIER);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void updateNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Create the Client

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc.perform(put("/api/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        // Initialize the database
        clientService.save(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Delete the client
        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
