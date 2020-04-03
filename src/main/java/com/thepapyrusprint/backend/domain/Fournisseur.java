package com.thepapyrusprint.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Fournisseur.
 */
@Entity
@Table(name = "fournisseur")
public class Fournisseur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "ville", nullable = false)
    private String ville;

    @NotNull
    @Column(name = "pays", nullable = false)
    private String pays;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "fournisseur")
    private Set<Commande> commandes = new HashSet<>();

    @ManyToMany(mappedBy = "fournisseurs")
    @JsonIgnore
    private Set<Catalogue> catalogues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Fournisseur nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public Fournisseur ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public Fournisseur pays(String pays) {
        this.pays = pays;
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getTelephone() {
        return telephone;
    }

    public Fournisseur telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public Fournisseur email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public Fournisseur commandes(Set<Commande> commandes) {
        this.commandes = commandes;
        return this;
    }

    public Fournisseur addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.setFournisseur(this);
        return this;
    }

    public Fournisseur removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.setFournisseur(null);
        return this;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }

    public Set<Catalogue> getCatalogues() {
        return catalogues;
    }

    public Fournisseur catalogues(Set<Catalogue> catalogues) {
        this.catalogues = catalogues;
        return this;
    }

    public Fournisseur addCatalogue(Catalogue catalogue) {
        this.catalogues.add(catalogue);
        catalogue.getFournisseurs().add(this);
        return this;
    }

    public Fournisseur removeCatalogue(Catalogue catalogue) {
        this.catalogues.remove(catalogue);
        catalogue.getFournisseurs().remove(this);
        return this;
    }

    public void setCatalogues(Set<Catalogue> catalogues) {
        this.catalogues = catalogues;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fournisseur)) {
            return false;
        }
        return id != null && id.equals(((Fournisseur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", ville='" + getVille() + "'" +
            ", pays='" + getPays() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
