package com.thepapyrusprint.backend.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Point.
 */
@Entity
@Table(name = "point")
public class Point implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre_point", nullable = false)
    private Float nombrePoint;

    @Column(name = "observation")
    private String observation;

    @OneToOne
    @JoinColumn(unique = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getNombrePoint() {
        return nombrePoint;
    }

    public Point nombrePoint(Float nombrePoint) {
        this.nombrePoint = nombrePoint;
        return this;
    }

    public void setNombrePoint(Float nombrePoint) {
        this.nombrePoint = nombrePoint;
    }

    public String getObservation() {
        return observation;
    }

    public Point observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Client getClient() {
        return client;
    }

    public Point client(Client client) {
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
        if (!(o instanceof Point)) {
            return false;
        }
        return id != null && id.equals(((Point) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Point{" +
            "id=" + getId() +
            ", nombrePoint=" + getNombrePoint() +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
