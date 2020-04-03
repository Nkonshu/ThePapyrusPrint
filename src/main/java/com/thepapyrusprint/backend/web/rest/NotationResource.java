package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.domain.Notation;
import com.thepapyrusprint.backend.repository.NotationRepository;
import com.thepapyrusprint.backend.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.thepapyrusprint.backend.domain.Notation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NotationResource {

    private final Logger log = LoggerFactory.getLogger(NotationResource.class);

    private static final String ENTITY_NAME = "notation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotationRepository notationRepository;

    public NotationResource(NotationRepository notationRepository) {
        this.notationRepository = notationRepository;
    }

    /**
     * {@code POST  /notations} : Create a new notation.
     *
     * @param notation the notation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notation, or with status {@code 400 (Bad Request)} if the notation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notations")
    public ResponseEntity<Notation> createNotation(@RequestBody Notation notation) throws URISyntaxException {
        log.debug("REST request to save Notation : {}", notation);
        if (notation.getId() != null) {
            throw new BadRequestAlertException("A new notation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Notation result = notationRepository.save(notation);
        return ResponseEntity.created(new URI("/api/notations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notations} : Updates an existing notation.
     *
     * @param notation the notation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notation,
     * or with status {@code 400 (Bad Request)} if the notation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notations")
    public ResponseEntity<Notation> updateNotation(@RequestBody Notation notation) throws URISyntaxException {
        log.debug("REST request to update Notation : {}", notation);
        if (notation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Notation result = notationRepository.save(notation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notations} : get all the notations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notations in body.
     */
    @GetMapping("/notations")
    public List<Notation> getAllNotations() {
        log.debug("REST request to get all Notations");
        return notationRepository.findAll();
    }

    /**
     * {@code GET  /notations/:id} : get the "id" notation.
     *
     * @param id the id of the notation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notations/{id}")
    public ResponseEntity<Notation> getNotation(@PathVariable Long id) {
        log.debug("REST request to get Notation : {}", id);
        Optional<Notation> notation = notationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(notation);
    }

    /**
     * {@code DELETE  /notations/:id} : delete the "id" notation.
     *
     * @param id the id of the notation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notations/{id}")
    public ResponseEntity<Void> deleteNotation(@PathVariable Long id) {
        log.debug("REST request to delete Notation : {}", id);
        notationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
