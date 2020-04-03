package com.thepapyrusprint.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.thepapyrusprint.backend.domain.enumeration.OrderStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.thepapyrusprint.backend.domain.Facture} entity. This class is used
 * in {@link com.thepapyrusprint.backend.web.rest.FactureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /factures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FactureCriteria implements Serializable, Criteria {
    /**
     * Class for filtering OrderStatus
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {

        public OrderStatusFilter() {
        }

        public OrderStatusFilter(OrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public OrderStatusFilter copy() {
            return new OrderStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date;

    private StringFilter numero;

    private LocalDateFilter dateLivraison;

    private StringFilter lieuxLivraison;

    private OrderStatusFilter orderStatus;

    private StringFilter observation;

    private LongFilter produitId;

    private LongFilter clientId;

    public FactureCriteria() {
    }

    public FactureCriteria(FactureCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.numero = other.numero == null ? null : other.numero.copy();
        this.dateLivraison = other.dateLivraison == null ? null : other.dateLivraison.copy();
        this.lieuxLivraison = other.lieuxLivraison == null ? null : other.lieuxLivraison.copy();
        this.orderStatus = other.orderStatus == null ? null : other.orderStatus.copy();
        this.observation = other.observation == null ? null : other.observation.copy();
        this.produitId = other.produitId == null ? null : other.produitId.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
    }

    @Override
    public FactureCriteria copy() {
        return new FactureCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public StringFilter getNumero() {
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public LocalDateFilter getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDateFilter dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public StringFilter getLieuxLivraison() {
        return lieuxLivraison;
    }

    public void setLieuxLivraison(StringFilter lieuxLivraison) {
        this.lieuxLivraison = lieuxLivraison;
    }

    public OrderStatusFilter getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusFilter orderStatus) {
        this.orderStatus = orderStatus;
    }

    public StringFilter getObservation() {
        return observation;
    }

    public void setObservation(StringFilter observation) {
        this.observation = observation;
    }

    public LongFilter getProduitId() {
        return produitId;
    }

    public void setProduitId(LongFilter produitId) {
        this.produitId = produitId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FactureCriteria that = (FactureCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(dateLivraison, that.dateLivraison) &&
            Objects.equals(lieuxLivraison, that.lieuxLivraison) &&
            Objects.equals(orderStatus, that.orderStatus) &&
            Objects.equals(observation, that.observation) &&
            Objects.equals(produitId, that.produitId) &&
            Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        date,
        numero,
        dateLivraison,
        lieuxLivraison,
        orderStatus,
        observation,
        produitId,
        clientId
        );
    }

    @Override
    public String toString() {
        return "FactureCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (numero != null ? "numero=" + numero + ", " : "") +
                (dateLivraison != null ? "dateLivraison=" + dateLivraison + ", " : "") +
                (lieuxLivraison != null ? "lieuxLivraison=" + lieuxLivraison + ", " : "") +
                (orderStatus != null ? "orderStatus=" + orderStatus + ", " : "") +
                (observation != null ? "observation=" + observation + ", " : "") +
                (produitId != null ? "produitId=" + produitId + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
            "}";
    }

}
