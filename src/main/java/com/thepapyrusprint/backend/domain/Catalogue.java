package com.thepapyrusprint.backend.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Catalogue.
 */
@Entity
@Table(name = "catalogue")
public class Catalogue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "observation")
    private String observation;

    @ManyToMany
    @JoinTable(name = "catalogue_produit",
               joinColumns = @JoinColumn(name = "catalogue_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "produit_id", referencedColumnName = "id"))
    private Set<Produit> produits = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "catalogue_fournisseur",
               joinColumns = @JoinColumn(name = "catalogue_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "fournisseur_id", referencedColumnName = "id"))
    private Set<Fournisseur> fournisseurs = new HashSet<>();

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

    public Catalogue nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getObservation() {
        return observation;
    }

    public Catalogue observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public Catalogue produits(Set<Produit> produits) {
        this.produits = produits;
        return this;
    }

    public Catalogue addProduit(Produit produit) {
        this.produits.add(produit);
        produit.getCatalogues().add(this);
        return this;
    }

    public Catalogue removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.getCatalogues().remove(this);
        return this;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }

    public Set<Fournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public Catalogue fournisseurs(Set<Fournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
        return this;
    }

    public Catalogue addFournisseur(Fournisseur fournisseur) {
        this.fournisseurs.add(fournisseur);
        fournisseur.getCatalogues().add(this);
        return this;
    }

    public Catalogue removeFournisseur(Fournisseur fournisseur) {
        this.fournisseurs.remove(fournisseur);
        fournisseur.getCatalogues().remove(this);
        return this;
    }

    public void setFournisseurs(Set<Fournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catalogue)) {
            return false;
        }
        return id != null && id.equals(((Catalogue) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Catalogue{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
