package com.thepapyrusprint.backend.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.thepapyrusprint.backend.domain.Catalogue;
import com.thepapyrusprint.backend.domain.*; // for static metamodels
import com.thepapyrusprint.backend.repository.CatalogueRepository;
import com.thepapyrusprint.backend.service.dto.CatalogueCriteria;

/**
 * Service for executing complex queries for {@link Catalogue} entities in the database.
 * The main input is a {@link CatalogueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Catalogue} or a {@link Page} of {@link Catalogue} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CatalogueQueryService extends QueryService<Catalogue> {

    private final Logger log = LoggerFactory.getLogger(CatalogueQueryService.class);

    private final CatalogueRepository catalogueRepository;

    public CatalogueQueryService(CatalogueRepository catalogueRepository) {
        this.catalogueRepository = catalogueRepository;
    }

    /**
     * Return a {@link List} of {@link Catalogue} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Catalogue> findByCriteria(CatalogueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Catalogue> specification = createSpecification(criteria);
        return catalogueRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Catalogue} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Catalogue> findByCriteria(CatalogueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Catalogue> specification = createSpecification(criteria);
        return catalogueRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CatalogueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Catalogue> specification = createSpecification(criteria);
        return catalogueRepository.count(specification);
    }

    /**
     * Function to convert {@link CatalogueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Catalogue> createSpecification(CatalogueCriteria criteria) {
        Specification<Catalogue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Catalogue_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Catalogue_.nom));
            }
            if (criteria.getObservation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservation(), Catalogue_.observation));
            }
            if (criteria.getProduitId() != null) {
                specification = specification.and(buildSpecification(criteria.getProduitId(),
                    root -> root.join(Catalogue_.produits, JoinType.LEFT).get(Produit_.id)));
            }
            if (criteria.getFournisseurId() != null) {
                specification = specification.and(buildSpecification(criteria.getFournisseurId(),
                    root -> root.join(Catalogue_.fournisseurs, JoinType.LEFT).get(Fournisseur_.id)));
            }
        }
        return specification;
    }
}
