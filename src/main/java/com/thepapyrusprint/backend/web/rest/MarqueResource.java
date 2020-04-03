package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.domain.Marque;
import com.thepapyrusprint.backend.service.MarqueService;
import com.thepapyrusprint.backend.web.rest.errors.BadRequestAlertException;
import com.thepapyrusprint.backend.service.dto.MarqueCriteria;
import com.thepapyrusprint.backend.service.MarqueQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.thepapyrusprint.backend.domain.Marque}.
 */
@RestController
@RequestMapping("/api")
public class MarqueResource {

    private final Logger log = LoggerFactory.getLogger(MarqueResource.class);

    private static final String ENTITY_NAME = "marque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarqueService marqueService;

    private final MarqueQueryService marqueQueryService;

    public MarqueResource(MarqueService marqueService, MarqueQueryService marqueQueryService) {
        this.marqueService = marqueService;
        this.marqueQueryService = marqueQueryService;
    }

    /**
     * {@code POST  /marques} : Create a new marque.
     *
     * @param marque the marque to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marque, or with status {@code 400 (Bad Request)} if the marque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/marques")
    public ResponseEntity<Marque> createMarque(@Valid @RequestBody Marque marque) throws URISyntaxException {
        log.debug("REST request to save Marque : {}", marque);
        if (marque.getId() != null) {
            throw new BadRequestAlertException("A new marque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Marque result = marqueService.save(marque);
        return ResponseEntity.created(new URI("/api/marques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /marques} : Updates an existing marque.
     *
     * @param marque the marque to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marque,
     * or with status {@code 400 (Bad Request)} if the marque is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marque couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/marques")
    public ResponseEntity<Marque> updateMarque(@Valid @RequestBody Marque marque) throws URISyntaxException {
        log.debug("REST request to update Marque : {}", marque);
        if (marque.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Marque result = marqueService.save(marque);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marque.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /marques} : get all the marques.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marques in body.
     */
    @GetMapping("/marques")
    public ResponseEntity<List<Marque>> getAllMarques(MarqueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Marques by criteria: {}", criteria);
        Page<Marque> page = marqueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /marques/count} : count all the marques.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/marques/count")
    public ResponseEntity<Long> countMarques(MarqueCriteria criteria) {
        log.debug("REST request to count Marques by criteria: {}", criteria);
        return ResponseEntity.ok().body(marqueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /marques/:id} : get the "id" marque.
     *
     * @param id the id of the marque to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marque, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/marques/{id}")
    public ResponseEntity<Marque> getMarque(@PathVariable Long id) {
        log.debug("REST request to get Marque : {}", id);
        Optional<Marque> marque = marqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(marque);
    }

    /**
     * {@code DELETE  /marques/:id} : delete the "id" marque.
     *
     * @param id the id of the marque to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/marques/{id}")
    public ResponseEntity<Void> deleteMarque(@PathVariable Long id) {
        log.debug("REST request to delete Marque : {}", id);
        marqueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
