package com.thepapyrusprint.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Produit.
 */
@Entity
@Table(name = "produit")
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prix_vente_htva", nullable = false)
    private Float prixVenteHtva;

    @NotNull
    @Column(name = "prix_vente_tvac", nullable = false)
    private Float prixVenteTvac;

    @NotNull
    @Column(name = "prix_achat_ht", nullable = false)
    private Float prixAchatHt;

    @NotNull
    @Column(name = "taux_tva_achat", nullable = false)
    private Float tauxTvaAchat;

    @NotNull
    @Column(name = "taux_tva", nullable = false)
    private Float tauxTva;

    @Column(name = "observation")
    private String observation;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(name = "produit_notation",
               joinColumns = @JoinColumn(name = "produit_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "notation_id", referencedColumnName = "id"))
    private Set<Notation> notations = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "produit_image",
               joinColumns = @JoinColumn(name = "produit_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
    private Set<Image> images = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("produits")
    private Marque marque;

    @ManyToMany(mappedBy = "produits")
    @JsonIgnore
    private Set<Catalogue> catalogues = new HashSet<>();

    @ManyToMany(mappedBy = "produits")
    @JsonIgnore
    private Set<Commande> commandes = new HashSet<>();

    @ManyToMany(mappedBy = "produits")
    @JsonIgnore
    private Set<Facture> factures = new HashSet<>();

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

    public Produit nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Float getPrixVenteHtva() {
        return prixVenteHtva;
    }

    public Produit prixVenteHtva(Float prixVenteHtva) {
        this.prixVenteHtva = prixVenteHtva;
        return this;
    }

    public void setPrixVenteHtva(Float prixVenteHtva) {
        this.prixVenteHtva = prixVenteHtva;
    }

    public Float getPrixVenteTvac() {
        return prixVenteTvac;
    }

    public Produit prixVenteTvac(Float prixVenteTvac) {
        this.prixVenteTvac = prixVenteTvac;
        return this;
    }

    public void setPrixVenteTvac(Float prixVenteTvac) {
        this.prixVenteTvac = prixVenteTvac;
    }

    public Float getPrixAchatHt() {
        return prixAchatHt;
    }

    public Produit prixAchatHt(Float prixAchatHt) {
        this.prixAchatHt = prixAchatHt;
        return this;
    }

    public void setPrixAchatHt(Float prixAchatHt) {
        this.prixAchatHt = prixAchatHt;
    }

    public Float getTauxTvaAchat() {
        return tauxTvaAchat;
    }

    public Produit tauxTvaAchat(Float tauxTvaAchat) {
        this.tauxTvaAchat = tauxTvaAchat;
        return this;
    }

    public void setTauxTvaAchat(Float tauxTvaAchat) {
        this.tauxTvaAchat = tauxTvaAchat;
    }

    public Float getTauxTva() {
        return tauxTva;
    }

    public Produit tauxTva(Float tauxTva) {
        this.tauxTva = tauxTva;
        return this;
    }

    public void setTauxTva(Float tauxTva) {
        this.tauxTva = tauxTva;
    }

    public String getObservation() {
        return observation;
    }

    public Produit observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getDescription() {
        return description;
    }

    public Produit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Notation> getNotations() {
        return notations;
    }

    public Produit notations(Set<Notation> notations) {
        this.notations = notations;
        return this;
    }

    public Produit addNotation(Notation notation) {
        this.notations.add(notation);
        notation.getProduits().add(this);
        return this;
    }

    public Produit removeNotation(Notation notation) {
        this.notations.remove(notation);
        notation.getProduits().remove(this);
        return this;
    }

    public void setNotations(Set<Notation> notations) {
        this.notations = notations;
    }

    public Set<Image> getImages() {
        return images;
    }

    public Produit images(Set<Image> images) {
        this.images = images;
        return this;
    }

    public Produit addImage(Image image) {
        this.images.add(image);
        image.getProduits().add(this);
        return this;
    }

    public Produit removeImage(Image image) {
        this.images.remove(image);
        image.getProduits().remove(this);
        return this;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Marque getMarque() {
        return marque;
    }

    public Produit marque(Marque marque) {
        this.marque = marque;
        return this;
    }

    public void setMarque(Marque marque) {
        this.marque = marque;
    }

    public Set<Catalogue> getCatalogues() {
        return catalogues;
    }

    public Produit catalogues(Set<Catalogue> catalogues) {
        this.catalogues = catalogues;
        return this;
    }

    public Produit addCatalogue(Catalogue catalogue) {
        this.catalogues.add(catalogue);
        catalogue.getProduits().add(this);
        return this;
    }

    public Produit removeCatalogue(Catalogue catalogue) {
        this.catalogues.remove(catalogue);
        catalogue.getProduits().remove(this);
        return this;
    }

    public void setCatalogues(Set<Catalogue> catalogues) {
        this.catalogues = catalogues;
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public Produit commandes(Set<Commande> commandes) {
        this.commandes = commandes;
        return this;
    }

    public Produit addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.getProduits().add(this);
        return this;
    }

    public Produit removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.getProduits().remove(this);
        return this;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }

    public Set<Facture> getFactures() {
        return factures;
    }

    public Produit factures(Set<Facture> factures) {
        this.factures = factures;
        return this;
    }

    public Produit addFacture(Facture facture) {
        this.factures.add(facture);
        facture.getProduits().add(this);
        return this;
    }

    public Produit removeFacture(Facture facture) {
        this.factures.remove(facture);
        facture.getProduits().remove(this);
        return this;
    }

    public void setFactures(Set<Facture> factures) {
        this.factures = factures;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Produit)) {
            return false;
        }
        return id != null && id.equals(((Produit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Produit{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prixVenteHtva=" + getPrixVenteHtva() +
            ", prixVenteTvac=" + getPrixVenteTvac() +
            ", prixAchatHt=" + getPrixAchatHt() +
            ", tauxTvaAchat=" + getTauxTvaAchat() +
            ", tauxTva=" + getTauxTva() +
            ", observation='" + getObservation() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
