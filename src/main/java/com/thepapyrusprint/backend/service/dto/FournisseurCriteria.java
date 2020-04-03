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
 * Criteria class for the {@link com.thepapyrusprint.backend.domain.Fournisseur} entity. This class is used
 * in {@link com.thepapyrusprint.backend.web.rest.FournisseurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fournisseurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FournisseurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter ville;

    private StringFilter pays;

    private StringFilter telephone;

    private StringFilter email;

    private LongFilter commandeId;

    private LongFilter catalogueId;

    public FournisseurCriteria() {
    }

    public FournisseurCriteria(FournisseurCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.ville = other.ville == null ? null : other.ville.copy();
        this.pays = other.pays == null ? null : other.pays.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.commandeId = other.commandeId == null ? null : other.commandeId.copy();
        this.catalogueId = other.catalogueId == null ? null : other.catalogueId.copy();
    }

    @Override
    public FournisseurCriteria copy() {
        return new FournisseurCriteria(this);
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

    public StringFilter getVille() {
        return ville;
    }

    public void setVille(StringFilter ville) {
        this.ville = ville;
    }

    public StringFilter getPays() {
        return pays;
    }

    public void setPays(StringFilter pays) {
        this.pays = pays;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(LongFilter commandeId) {
        this.commandeId = commandeId;
    }

    public LongFilter getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(LongFilter catalogueId) {
        this.catalogueId = catalogueId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FournisseurCriteria that = (FournisseurCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(ville, that.ville) &&
            Objects.equals(pays, that.pays) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(commandeId, that.commandeId) &&
            Objects.equals(catalogueId, that.catalogueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nom,
        ville,
        pays,
        telephone,
        email,
        commandeId,
        catalogueId
        );
    }

    @Override
    public String toString() {
        return "FournisseurCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (ville != null ? "ville=" + ville + ", " : "") +
                (pays != null ? "pays=" + pays + ", " : "") +
                (telephone != null ? "telephone=" + telephone + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (commandeId != null ? "commandeId=" + commandeId + ", " : "") +
                (catalogueId != null ? "catalogueId=" + catalogueId + ", " : "") +
            "}";
    }

}
