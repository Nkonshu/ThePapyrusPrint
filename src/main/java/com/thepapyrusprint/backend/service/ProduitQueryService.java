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

import com.thepapyrusprint.backend.domain.Produit;
import com.thepapyrusprint.backend.domain.*; // for static metamodels
import com.thepapyrusprint.backend.repository.ProduitRepository;
import com.thepapyrusprint.backend.service.dto.ProduitCriteria;

/**
 * Service for executing complex queries for {@link Produit} entities in the database.
 * The main input is a {@link ProduitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Produit} or a {@link Page} of {@link Produit} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProduitQueryService extends QueryService<Produit> {

    private final Logger log = LoggerFactory.getLogger(ProduitQueryService.class);

    private final ProduitRepository produitRepository;

    public ProduitQueryService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    /**
     * Return a {@link List} of {@link Produit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Produit> findByCriteria(ProduitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Produit> specification = createSpecification(criteria);
        return produitRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Produit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Produit> findByCriteria(ProduitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Produit> specification = createSpecification(criteria);
        return produitRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProduitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Produit> specification = createSpecification(criteria);
        return produitRepository.count(specification);
    }

    /**
     * Function to convert {@link ProduitCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Produit> createSpecification(ProduitCriteria criteria) {
        Specification<Produit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Produit_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Produit_.nom));
            }
            if (criteria.getPrixVenteHtva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrixVenteHtva(), Produit_.prixVenteHtva));
            }
            if (criteria.getPrixVenteTvac() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrixVenteTvac(), Produit_.prixVenteTvac));
            }
            if (criteria.getPrixAchatHt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrixAchatHt(), Produit_.prixAchatHt));
            }
            if (criteria.getTauxTvaAchat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTauxTvaAchat(), Produit_.tauxTvaAchat));
            }
            if (criteria.getTauxTva() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTauxTva(), Produit_.tauxTva));
            }
            if (criteria.getObservation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservation(), Produit_.observation));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Produit_.description));
            }
            if (criteria.getNotationId() != null) {
                specification = specification.and(buildSpecification(criteria.getNotationId(),
                    root -> root.join(Produit_.notations, JoinType.LEFT).get(Notation_.id)));
            }
            if (criteria.getImageId() != null) {
                specification = specification.and(buildSpecification(criteria.getImageId(),
                    root -> root.join(Produit_.images, JoinType.LEFT).get(Image_.id)));
            }
            if (criteria.getMarqueId() != null) {
                specification = specification.and(buildSpecification(criteria.getMarqueId(),
                    root -> root.join(Produit_.marque, JoinType.LEFT).get(Marque_.id)));
            }
            if (criteria.getCatalogueId() != null) {
                specification = specification.and(buildSpecification(criteria.getCatalogueId(),
                    root -> root.join(Produit_.catalogues, JoinType.LEFT).get(Catalogue_.id)));
            }
            if (criteria.getCommandeId() != null) {
                specification = specification.and(buildSpecification(criteria.getCommandeId(),
                    root -> root.join(Produit_.commandes, JoinType.LEFT).get(Commande_.id)));
            }
            if (criteria.getFactureId() != null) {
                specification = specification.and(buildSpecification(criteria.getFactureId(),
                    root -> root.join(Produit_.factures, JoinType.LEFT).get(Facture_.id)));
            }
        }
        return specification;
    }
}
