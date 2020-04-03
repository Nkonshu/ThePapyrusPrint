package com.thepapyrusprint.backend.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_max")
    private String stockMax;

    @Column(name = "stock_min")
    private String stockMin;

    @NotNull
    @Column(name = "quantite_produit", nullable = false)
    private String quantiteProduit;

    @Column(name = "observation")
    private String observation;

    @OneToOne
    @JoinColumn(unique = true)
    private Produit produit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockMax() {
        return stockMax;
    }

    public Stock stockMax(String stockMax) {
        this.stockMax = stockMax;
        return this;
    }

    public void setStockMax(String stockMax) {
        this.stockMax = stockMax;
    }

    public String getStockMin() {
        return stockMin;
    }

    public Stock stockMin(String stockMin) {
        this.stockMin = stockMin;
        return this;
    }

    public void setStockMin(String stockMin) {
        this.stockMin = stockMin;
    }

    public String getQuantiteProduit() {
        return quantiteProduit;
    }

    public Stock quantiteProduit(String quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
        return this;
    }

    public void setQuantiteProduit(String quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
    }

    public String getObservation() {
        return observation;
    }

    public Stock observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Produit getProduit() {
        return produit;
    }

    public Stock produit(Produit produit) {
        this.produit = produit;
        return this;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock)) {
            return false;
        }
        return id != null && id.equals(((Stock) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", stockMax='" + getStockMax() + "'" +
            ", stockMin='" + getStockMin() + "'" +
            ", quantiteProduit='" + getQuantiteProduit() + "'" +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
