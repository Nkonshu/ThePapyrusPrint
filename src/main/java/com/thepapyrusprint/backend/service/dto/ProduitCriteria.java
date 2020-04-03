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
 * Criteria class for the {@link com.thepapyrusprint.backend.domain.Produit} entity. This class is used
 * in {@link com.thepapyrusprint.backend.web.rest.ProduitResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /produits?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProduitCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private FloatFilter prixVenteHtva;

    private FloatFilter prixVenteTvac;

    private FloatFilter prixAchatHt;

    private FloatFilter tauxTvaAchat;

    private FloatFilter tauxTva;

    private StringFilter observation;

    private StringFilter description;

    private LongFilter notationId;

    private LongFilter imageId;

    private LongFilter marqueId;

    private LongFilter catalogueId;

    private LongFilter commandeId;

    private LongFilter factureId;

    public ProduitCriteria() {
    }

    public ProduitCriteria(ProduitCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.prixVenteHtva = other.prixVenteHtva == null ? null : other.prixVenteHtva.copy();
        this.prixVenteTvac = other.prixVenteTvac == null ? null : other.prixVenteTvac.copy();
        this.prixAchatHt = other.prixAchatHt == null ? null : other.prixAchatHt.copy();
        this.tauxTvaAchat = other.tauxTvaAchat == null ? null : other.tauxTvaAchat.copy();
        this.tauxTva = other.tauxTva == null ? null : other.tauxTva.copy();
        this.observation = other.observation == null ? null : other.observation.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.notationId = other.notationId == null ? null : other.notationId.copy();
        this.imageId = other.imageId == null ? null : other.imageId.copy();
        this.marqueId = other.marqueId == null ? null : other.marqueId.copy();
        this.catalogueId = other.catalogueId == null ? null : other.catalogueId.copy();
        this.commandeId = other.commandeId == null ? null : other.commandeId.copy();
        this.factureId = other.factureId == null ? null : other.factureId.copy();
    }

    @Override
    public ProduitCriteria copy() {
        return new ProduitCriteria(this);
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

    public FloatFilter getPrixVenteHtva() {
        return prixVenteHtva;
    }

    public void setPrixVenteHtva(FloatFilter prixVenteHtva) {
        this.prixVenteHtva = prixVenteHtva;
    }

    public FloatFilter getPrixVenteTvac() {
        return prixVenteTvac;
    }

    public void setPrixVenteTvac(FloatFilter prixVenteTvac) {
        this.prixVenteTvac = prixVenteTvac;
    }

    public FloatFilter getPrixAchatHt() {
        return prixAchatHt;
    }

    public void setPrixAchatHt(FloatFilter prixAchatHt) {
        this.prixAchatHt = prixAchatHt;
    }

    public FloatFilter getTauxTvaAchat() {
        return tauxTvaAchat;
    }

    public void setTauxTvaAchat(FloatFilter tauxTvaAchat) {
        this.tauxTvaAchat = tauxTvaAchat;
    }

    public FloatFilter getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(FloatFilter tauxTva) {
        this.tauxTva = tauxTva;
    }

    public StringFilter getObservation() {
        return observation;
    }

    public void setObservation(StringFilter observation) {
        this.observation = observation;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getNotationId() {
        return notationId;
    }

    public void setNotationId(LongFilter notationId) {
        this.notationId = notationId;
    }

    public LongFilter getImageId() {
        return imageId;
    }

    public void setImageId(LongFilter imageId) {
        this.imageId = imageId;
    }

    public LongFilter getMarqueId() {
        return marqueId;
    }

    public void setMarqueId(LongFilter marqueId) {
        this.marqueId = marqueId;
    }

    public LongFilter getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(LongFilter catalogueId) {
        this.catalogueId = catalogueId;
    }

    public LongFilter getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(LongFilter commandeId) {
        this.commandeId = commandeId;
    }

    public LongFilter getFactureId() {
        return factureId;
    }

    public void setFactureId(LongFilter factureId) {
        this.factureId = factureId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProduitCriteria that = (ProduitCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prixVenteHtva, that.prixVenteHtva) &&
            Objects.equals(prixVenteTvac, that.prixVenteTvac) &&
            Objects.equals(prixAchatHt, that.prixAchatHt) &&
            Objects.equals(tauxTvaAchat, that.tauxTvaAchat) &&
            Objects.equals(tauxTva, that.tauxTva) &&
            Objects.equals(observation, that.observation) &&
            Objects.equals(description, that.description) &&
            Objects.equals(notationId, that.notationId) &&
            Objects.equals(imageId, that.imageId) &&
            Objects.equals(marqueId, that.marqueId) &&
            Objects.equals(catalogueId, that.catalogueId) &&
            Objects.equals(commandeId, that.commandeId) &&
            Objects.equals(factureId, that.factureId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nom,
        prixVenteHtva,
        prixVenteTvac,
        prixAchatHt,
        tauxTvaAchat,
        tauxTva,
        observation,
        description,
        notationId,
        imageId,
        marqueId,
        catalogueId,
        commandeId,
        factureId
        );
    }

    @Override
    public String toString() {
        return "ProduitCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (prixVenteHtva != null ? "prixVenteHtva=" + prixVenteHtva + ", " : "") +
                (prixVenteTvac != null ? "prixVenteTvac=" + prixVenteTvac + ", " : "") +
                (prixAchatHt != null ? "prixAchatHt=" + prixAchatHt + ", " : "") +
                (tauxTvaAchat != null ? "tauxTvaAchat=" + tauxTvaAchat + ", " : "") +
                (tauxTva != null ? "tauxTva=" + tauxTva + ", " : "") +
                (observation != null ? "observation=" + observation + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (notationId != null ? "notationId=" + notationId + ", " : "") +
                (imageId != null ? "imageId=" + imageId + ", " : "") +
                (marqueId != null ? "marqueId=" + marqueId + ", " : "") +
                (catalogueId != null ? "catalogueId=" + catalogueId + ", " : "") +
                (commandeId != null ? "commandeId=" + commandeId + ", " : "") +
                (factureId != null ? "factureId=" + factureId + ", " : "") +
            "}";
    }

}
