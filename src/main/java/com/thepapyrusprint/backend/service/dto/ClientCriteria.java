package com.thepapyrusprint.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.thepapyrusprint.backend.domain.enumeration.Civilite;
import com.thepapyrusprint.backend.domain.enumeration.Sexe;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.thepapyrusprint.backend.domain.Client} entity. This class is used
 * in {@link com.thepapyrusprint.backend.web.rest.ClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClientCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Civilite
     */
    public static class CiviliteFilter extends Filter<Civilite> {

        public CiviliteFilter() {
        }

        public CiviliteFilter(CiviliteFilter filter) {
            super(filter);
        }

        @Override
        public CiviliteFilter copy() {
            return new CiviliteFilter(this);
        }

    }
    /**
     * Class for filtering Sexe
     */
    public static class SexeFilter extends Filter<Sexe> {

        public SexeFilter() {
        }

        public SexeFilter(SexeFilter filter) {
            super(filter);
        }

        @Override
        public SexeFilter copy() {
            return new SexeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private CiviliteFilter civilite;

    private StringFilter nom;

    private StringFilter prenom;

    private SexeFilter sexe;

    private LocalDateFilter dateNaissance;

    private StringFilter telephone;

    private StringFilter ville;

    private StringFilter codePostal;

    private StringFilter pays;

    private StringFilter quartier;

    private StringFilter email;

    private StringFilter observation;

    private LongFilter userId;

    private LongFilter imageId;

    private LongFilter factureId;

    public ClientCriteria() {
    }

    public ClientCriteria(ClientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.civilite = other.civilite == null ? null : other.civilite.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.sexe = other.sexe == null ? null : other.sexe.copy();
        this.dateNaissance = other.dateNaissance == null ? null : other.dateNaissance.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.ville = other.ville == null ? null : other.ville.copy();
        this.codePostal = other.codePostal == null ? null : other.codePostal.copy();
        this.pays = other.pays == null ? null : other.pays.copy();
        this.quartier = other.quartier == null ? null : other.quartier.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.observation = other.observation == null ? null : other.observation.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.imageId = other.imageId == null ? null : other.imageId.copy();
        this.factureId = other.factureId == null ? null : other.factureId.copy();
    }

    @Override
    public ClientCriteria copy() {
        return new ClientCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public CiviliteFilter getCivilite() {
        return civilite;
    }

    public void setCivilite(CiviliteFilter civilite) {
        this.civilite = civilite;
    }

    public StringFilter getNom() {
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public SexeFilter getSexe() {
        return sexe;
    }

    public void setSexe(SexeFilter sexe) {
        this.sexe = sexe;
    }

    public LocalDateFilter getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDateFilter dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getVille() {
        return ville;
    }

    public void setVille(StringFilter ville) {
        this.ville = ville;
    }

    public StringFilter getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(StringFilter codePostal) {
        this.codePostal = codePostal;
    }

    public StringFilter getPays() {
        return pays;
    }

    public void setPays(StringFilter pays) {
        this.pays = pays;
    }

    public StringFilter getQuartier() {
        return quartier;
    }

    public void setQuartier(StringFilter quartier) {
        this.quartier = quartier;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getObservation() {
        return observation;
    }

    public void setObservation(StringFilter observation) {
        this.observation = observation;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getImageId() {
        return imageId;
    }

    public void setImageId(LongFilter imageId) {
        this.imageId = imageId;
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
        final ClientCriteria that = (ClientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(civilite, that.civilite) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(ville, that.ville) &&
            Objects.equals(codePostal, that.codePostal) &&
            Objects.equals(pays, that.pays) &&
            Objects.equals(quartier, that.quartier) &&
            Objects.equals(email, that.email) &&
            Objects.equals(observation, that.observation) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(imageId, that.imageId) &&
            Objects.equals(factureId, that.factureId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        civilite,
        nom,
        prenom,
        sexe,
        dateNaissance,
        telephone,
        ville,
        codePostal,
        pays,
        quartier,
        email,
        observation,
        userId,
        imageId,
        factureId
        );
    }

    @Override
    public String toString() {
        return "ClientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (civilite != null ? "civilite=" + civilite + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (prenom != null ? "prenom=" + prenom + ", " : "") +
                (sexe != null ? "sexe=" + sexe + ", " : "") +
                (dateNaissance != null ? "dateNaissance=" + dateNaissance + ", " : "") +
                (telephone != null ? "telephone=" + telephone + ", " : "") +
                (ville != null ? "ville=" + ville + ", " : "") +
                (codePostal != null ? "codePostal=" + codePostal + ", " : "") +
                (pays != null ? "pays=" + pays + ", " : "") +
                (quartier != null ? "quartier=" + quartier + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (observation != null ? "observation=" + observation + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (imageId != null ? "imageId=" + imageId + ", " : "") +
                (factureId != null ? "factureId=" + factureId + ", " : "") +
            "}";
    }

}
