package com.thepapyrusprint.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Notation.
 */
@Entity
@Table(name = "notation")
public class Notation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note")
    private Float note;

    @Column(name = "observation")
    private String observation;

    @ManyToMany(mappedBy = "notations")
    @JsonIgnore
    private Set<Produit> produits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getNote() {
        return note;
    }

    public Notation note(Float note) {
        this.note = note;
        return this;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public String getObservation() {
        return observation;
    }

    public Notation observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public Notation produits(Set<Produit> produits) {
        this.produits = produits;
        return this;
    }

    public Notation addProduit(Produit produit) {
        this.produits.add(produit);
        produit.getNotations().add(this);
        return this;
    }

    public Notation removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.getNotations().remove(this);
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
        if (!(o instanceof Notation)) {
            return false;
        }
        return id != null && id.equals(((Notation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Notation{" +
            "id=" + getId() +
            ", note=" + getNote() +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
