package com.illudtechzone.booking.domain;



import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Document(indexName = "invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "distance")
    private Long distance;

    @Column(name = "pickup_address")
    private String pickupAddress;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "jhi_cost")
    private Long cost;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDistance() {
        return distance;
    }

    public Invoice distance(Long distance) {
        this.distance = distance;
        return this;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public Invoice pickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
        return this;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public Invoice destinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
        return this;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public Long getCost() {
        return cost;
    }

    public Invoice cost(Long cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invoice invoice = (Invoice) o;
        if (invoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", distance=" + getDistance() +
            ", pickupAddress='" + getPickupAddress() + "'" +
            ", destinationAddress='" + getDestinationAddress() + "'" +
            ", cost=" + getCost() +
            "}";
    }
}
