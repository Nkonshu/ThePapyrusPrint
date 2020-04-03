package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.ThePapyrusPrintApp;
import com.thepapyrusprint.backend.domain.Notation;
import com.thepapyrusprint.backend.repository.NotationRepository;

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
 * Integration tests for the {@link NotationResource} REST controller.
 */
@SpringBootTest(classes = ThePapyrusPrintApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class NotationResourceIT {

    private static final Float DEFAULT_NOTE = 1F;
    private static final Float UPDATED_NOTE = 2F;

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    @Autowired
    private NotationRepository notationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotationMockMvc;

    private Notation notation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notation createEntity(EntityManager em) {
        Notation notation = new Notation()
            .note(DEFAULT_NOTE)
            .observation(DEFAULT_OBSERVATION);
        return notation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notation createUpdatedEntity(EntityManager em) {
        Notation notation = new Notation()
            .note(UPDATED_NOTE)
            .observation(UPDATED_OBSERVATION);
        return notation;
    }

    @BeforeEach
    public void initTest() {
        notation = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotation() throws Exception {
        int databaseSizeBeforeCreate = notationRepository.findAll().size();

        // Create the Notation
        restNotationMockMvc.perform(post("/api/notations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notation)))
            .andExpect(status().isCreated());

        // Validate the Notation in the database
        List<Notation> notationList = notationRepository.findAll();
        assertThat(notationList).hasSize(databaseSizeBeforeCreate + 1);
        Notation testNotation = notationList.get(notationList.size() - 1);
        assertThat(testNotation.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testNotation.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    public void createNotationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notationRepository.findAll().size();

        // Create the Notation with an existing ID
        notation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotationMockMvc.perform(post("/api/notations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notation)))
            .andExpect(status().isBadRequest());

        // Validate the Notation in the database
        List<Notation> notationList = notationRepository.findAll();
        assertThat(notationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotations() throws Exception {
        // Initialize the database
        notationRepository.saveAndFlush(notation);

        // Get all the notationList
        restNotationMockMvc.perform(get("/api/notations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notation.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }
    
    @Test
    @Transactional
    public void getNotation() throws Exception {
        // Initialize the database
        notationRepository.saveAndFlush(notation);

        // Get the notation
        restNotationMockMvc.perform(get("/api/notations/{id}", notation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notation.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }

    @Test
    @Transactional
    public void getNonExistingNotation() throws Exception {
        // Get the notation
        restNotationMockMvc.perform(get("/api/notations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotation() throws Exception {
        // Initialize the database
        notationRepository.saveAndFlush(notation);

        int databaseSizeBeforeUpdate = notationRepository.findAll().size();

        // Update the notation
        Notation updatedNotation = notationRepository.findById(notation.getId()).get();
        // Disconnect from session so that the updates on updatedNotation are not directly saved in db
        em.detach(updatedNotation);
        updatedNotation
            .note(UPDATED_NOTE)
            .observation(UPDATED_OBSERVATION);

        restNotationMockMvc.perform(put("/api/notations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotation)))
            .andExpect(status().isOk());

        // Validate the Notation in the database
        List<Notation> notationList = notationRepository.findAll();
        assertThat(notationList).hasSize(databaseSizeBeforeUpdate);
        Notation testNotation = notationList.get(notationList.size() - 1);
        assertThat(testNotation.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testNotation.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void updateNonExistingNotation() throws Exception {
        int databaseSizeBeforeUpdate = notationRepository.findAll().size();

        // Create the Notation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotationMockMvc.perform(put("/api/notations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notation)))
            .andExpect(status().isBadRequest());

        // Validate the Notation in the database
        List<Notation> notationList = notationRepository.findAll();
        assertThat(notationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotation() throws Exception {
        // Initialize the database
        notationRepository.saveAndFlush(notation);

        int databaseSizeBeforeDelete = notationRepository.findAll().size();

        // Delete the notation
        restNotationMockMvc.perform(delete("/api/notations/{id}", notation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notation> notationList = notationRepository.findAll();
        assertThat(notationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
