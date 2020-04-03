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

import com.thepapyrusprint.backend.domain.Marque;
import com.thepapyrusprint.backend.domain.*; // for static metamodels
import com.thepapyrusprint.backend.repository.MarqueRepository;
import com.thepapyrusprint.backend.service.dto.MarqueCriteria;

/**
 * Service for executing complex queries for {@link Marque} entities in the database.
 * The main input is a {@link MarqueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Marque} or a {@link Page} of {@link Marque} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MarqueQueryService extends QueryService<Marque> {

    private final Logger log = LoggerFactory.getLogger(MarqueQueryService.class);

    private final MarqueRepository marqueRepository;

    public MarqueQueryService(MarqueRepository marqueRepository) {
        this.marqueRepository = marqueRepository;
    }

    /**
     * Return a {@link List} of {@link Marque} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Marque> findByCriteria(MarqueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Marque> specification = createSpecification(criteria);
        return marqueRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Marque} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Marque> findByCriteria(MarqueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Marque> specification = createSpecification(criteria);
        return marqueRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MarqueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Marque> specification = createSpecification(criteria);
        return marqueRepository.count(specification);
    }

    /**
     * Function to convert {@link MarqueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Marque> createSpecification(MarqueCriteria criteria) {
        Specification<Marque> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Marque_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Marque_.nom));
            }
            if (criteria.getObservation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservation(), Marque_.observation));
            }
            if (criteria.getProduitId() != null) {
                specification = specification.and(buildSpecification(criteria.getProduitId(),
                    root -> root.join(Marque_.produits, JoinType.LEFT).get(Produit_.id)));
            }
        }
        return specification;
    }
}
