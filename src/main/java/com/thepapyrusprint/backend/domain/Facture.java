package com.thepapyrusprint.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.thepapyrusprint.backend.domain.enumeration.OrderStatus;

/**
 * A Facture.
 */
@Entity
@Table(name = "facture")
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "numero", nullable = false)
    private String numero;

    @NotNull
    @Column(name = "date_livraison", nullable = false)
    private LocalDate dateLivraison;

    @NotNull
    @Column(name = "lieux_livraison", nullable = false)
    private String lieuxLivraison;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "observation")
    private String observation;

    @ManyToMany
    @JoinTable(name = "facture_produit",
               joinColumns = @JoinColumn(name = "facture_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "produit_id", referencedColumnName = "id"))
    private Set<Produit> produits = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("factures")
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Facture date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNumero() {
        return numero;
    }

    public Facture numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDateLivraison() {
        return dateLivraison;
    }

    public Facture dateLivraison(LocalDate dateLivraison) {
        this.dateLivraison = dateLivraison;
        return this;
    }

    public void setDateLivraison(LocalDate dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getLieuxLivraison() {
        return lieuxLivraison;
    }

    public Facture lieuxLivraison(String lieuxLivraison) {
        this.lieuxLivraison = lieuxLivraison;
        return this;
    }

    public void setLieuxLivraison(String lieuxLivraison) {
        this.lieuxLivraison = lieuxLivraison;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Facture orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getObservation() {
        return observation;
    }

    public Facture observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public Facture produits(Set<Produit> produits) {
        this.produits = produits;
        return this;
    }

    public Facture addProduit(Produit produit) {
        this.produits.add(produit);
        produit.getFactures().add(this);
        return this;
    }

    public Facture removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.getFactures().remove(this);
        return this;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }

    public Client getClient() {
        return client;
    }

    public Facture client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facture)) {
            return false;
        }
        return id != null && id.equals(((Facture) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Facture{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", numero='" + getNumero() + "'" +
            ", dateLivraison='" + getDateLivraison() + "'" +
            ", lieuxLivraison='" + getLieuxLivraison() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
