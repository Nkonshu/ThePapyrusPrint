package com.thepapyrusprint.backend.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Marque.
 */
@Entity
@Table(name = "marque")
public class Marque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "observation")
    private String observation;

    @OneToMany(mappedBy = "marque")
    private Set<Produit> produits = new HashSet<>();

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

    public Marque nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getObservation() {
        return observation;
    }

    public Marque observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public Marque produits(Set<Produit> produits) {
        this.produits = produits;
        return this;
    }

    public Marque addProduit(Produit produit) {
        this.produits.add(produit);
        produit.setMarque(this);
        return this;
    }

    public Marque removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.setMarque(null);
        return this;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marque)) {
            return false;
        }
        return id != null && id.equals(((Marque) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Marque{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
