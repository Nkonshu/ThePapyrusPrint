package com.thepapyrusprint.backend.web.rest;

import com.thepapyrusprint.backend.domain.Catalogue;
import com.thepapyrusprint.backend.service.CatalogueService;
import com.thepapyrusprint.backend.web.rest.errors.BadRequestAlertException;
import com.thepapyrusprint.backend.service.dto.CatalogueCriteria;
import com.thepapyrusprint.backend.service.CatalogueQueryService;

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
 * REST controller for managing {@link com.thepapyrusprint.backend.domain.Catalogue}.
 */
@RestController
@RequestMapping("/api")
public class CatalogueResource {

    private final Logger log = LoggerFactory.getLogger(CatalogueResource.class);

    private static final String ENTITY_NAME = "catalogue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogueService catalogueService;

    private final CatalogueQueryService catalogueQueryService;

    public CatalogueResource(CatalogueService catalogueService, CatalogueQueryService catalogueQueryService) {
        this.catalogueService = catalogueService;
        this.catalogueQueryService = catalogueQueryService;
    }

    /**
     * {@code POST  /catalogues} : Create a new catalogue.
     *
     * @param catalogue the catalogue to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogue, or with status {@code 400 (Bad Request)} if the catalogue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalogues")
    public ResponseEntity<Catalogue> createCatalogue(@Valid @RequestBody Catalogue catalogue) throws URISyntaxException {
        log.debug("REST request to save Catalogue : {}", catalogue);
        if (catalogue.getId() != null) {
            throw new BadRequestAlertException("A new catalogue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Catalogue result = catalogueService.save(catalogue);
        return ResponseEntity.created(new URI("/api/catalogues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalogues} : Updates an existing catalogue.
     *
     * @param catalogue the catalogue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogue,
     * or with status {@code 400 (Bad Request)} if the catalogue is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalogues")
    public ResponseEntity<Catalogue> updateCatalogue(@Valid @RequestBody Catalogue catalogue) throws URISyntaxException {
        log.debug("REST request to update Catalogue : {}", catalogue);
        if (catalogue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Catalogue result = catalogueService.save(catalogue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogue.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /catalogues} : get all the catalogues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogues in body.
     */
    @GetMapping("/catalogues")
    public ResponseEntity<List<Catalogue>> getAllCatalogues(CatalogueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Catalogues by criteria: {}", criteria);
        Page<Catalogue> page = catalogueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalogues/count} : count all the catalogues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/catalogues/count")
    public ResponseEntity<Long> countCatalogues(CatalogueCriteria criteria) {
        log.debug("REST request to count Catalogues by criteria: {}", criteria);
        return ResponseEntity.ok().body(catalogueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /catalogues/:id} : get the "id" catalogue.
     *
     * @param id the id of the catalogue to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalogues/{id}")
    public ResponseEntity<Catalogue> getCatalogue(@PathVariable Long id) {
        log.debug("REST request to get Catalogue : {}", id);
        Optional<Catalogue> catalogue = catalogueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogue);
    }

    /**
     * {@code DELETE  /catalogues/:id} : delete the "id" catalogue.
     *
     * @param id the id of the catalogue to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalogues/{id}")
    public ResponseEntity<Void> deleteCatalogue(@PathVariable Long id) {
        log.debug("REST request to delete Catalogue : {}", id);
        catalogueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
