package com.thepapyrusprint.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.thepapyrusprint.backend.domain.Catalogue} entity. This class is used
 * in {@link com.thepapyrusprint.backend.web.rest.CatalogueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /catalogues?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CatalogueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter observation;

    private LongFilter produitId;

    private LongFilter fournisseurId;

    public CatalogueCriteria() {
    }

    public CatalogueCriteria(CatalogueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.observation = other.observation == null ? null : other.observation.copy();
        this.produitId = other.produitId == null ? null : other.produitId.copy();
        this.fournisseurId = other.fournisseurId == null ? null : other.fournisseurId.copy();
    }

    @Override
    public CatalogueCriteria copy() {
        return new CatalogueCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getObservation() {
        return observation;
    }

    public void setObservation(StringFilter observation) {
        this.observation = observation;
    }

    public LongFilter getProduitId() {
        return produitId;
    }

    public void setProduitId(LongFilter produitId) {
        this.produitId = produitId;
    }

    public LongFilter getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(LongFilter fournisseurId) {
        this.fournisseurId = fournisseurId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CatalogueCriteria that = (CatalogueCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(observation, that.observation) &&
            Objects.equals(produitId, that.produitId) &&
            Objects.equals(fournisseurId, that.fournisseurId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nom,
        observation,
        produitId,
        fournisseurId
        );
    }

    @Override
    public String toString() {
        return "CatalogueCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (observation != null ? "observation=" + observation + ", " : "") +
                (produitId != null ? "produitId=" + produitId + ", " : "") +
                (fournisseurId != null ? "fournisseurId=" + fournisseurId + ", " : "") +
            "}";
    }

}
