package com.thepapyrusprint.backend.service;

import com.thepapyrusprint.backend.domain.Catalogue;
import com.thepapyrusprint.backend.repository.CatalogueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Catalogue}.
 */
@Service
@Transactional
public class CatalogueService {

    private final Logger log = LoggerFactory.getLogger(CatalogueService.class);

    private final CatalogueRepository catalogueRepository;

    public CatalogueService(CatalogueRepository catalogueRepository) {
        this.catalogueRepository = catalogueRepository;
    }

    /**
     * Save a catalogue.
     *
     * @param catalogue the entity to save.
     * @return the persisted entity.
     */
    public Catalogue save(Catalogue catalogue) {
        log.debug("Request to save Catalogue : {}", catalogue);
        return catalogueRepository.save(catalogue);
    }

    /**
     * Get all the catalogues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Catalogue> findAll(Pageable pageable) {
        log.debug("Request to get all Catalogues");
        return catalogueRepository.findAll(pageable);
    }

    /**
     * Get all the catalogues with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Catalogue> findAllWithEagerRelationships(Pageable pageable) {
        return catalogueRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one catalogue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Catalogue> findOne(Long id) {
        log.debug("Request to get Catalogue : {}", id);
        return catalogueRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the catalogue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Catalogue : {}", id);
        catalogueRepository.deleteById(id);
    }
}
