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
 * Criteria class for the {@link com.thepapyrusprint.backend.domain.Stock} entity. This class is used
 * in {@link com.thepapyrusprint.backend.web.rest.StockResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stocks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter stockMax;

    private StringFilter stockMin;

    private StringFilter quantiteProduit;

    private StringFilter observation;

    private LongFilter produitId;

    public StockCriteria() {
    }

    public StockCriteria(StockCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.stockMax = other.stockMax == null ? null : other.stockMax.copy();
        this.stockMin = other.stockMin == null ? null : other.stockMin.copy();
        this.quantiteProduit = other.quantiteProduit == null ? null : other.quantiteProduit.copy();
        this.observation = other.observation == null ? null : other.observation.copy();
        this.produitId = other.produitId == null ? null : other.produitId.copy();
    }

    @Override
    public StockCriteria copy() {
        return new StockCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStockMax() {
        return stockMax;
    }

    public void setStockMax(StringFilter stockMax) {
        this.stockMax = stockMax;
    }

    public StringFilter getStockMin() {
        return stockMin;
    }

    public void setStockMin(StringFilter stockMin) {
        this.stockMin = stockMin;
    }

    public StringFilter getQuantiteProduit() {
        return quantiteProduit;
    }

    public void setQuantiteProduit(StringFilter quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StockCriteria that = (StockCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(stockMax, that.stockMax) &&
            Objects.equals(stockMin, that.stockMin) &&
            Objects.equals(quantiteProduit, that.quantiteProduit) &&
            Objects.equals(observation, that.observation) &&
            Objects.equals(produitId, that.produitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        stockMax,
        stockMin,
        quantiteProduit,
        observation,
        produitId
        );
    }

    @Override
    public String toString() {
        return "StockCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (stockMax != null ? "stockMax=" + stockMax + ", " : "") +
                (stockMin != null ? "stockMin=" + stockMin + ", " : "") +
                (quantiteProduit != null ? "quantiteProduit=" + quantiteProduit + ", " : "") +
                (observation != null ? "observation=" + observation + ", " : "") +
                (produitId != null ? "produitId=" + produitId + ", " : "") +
            "}";
    }

}
