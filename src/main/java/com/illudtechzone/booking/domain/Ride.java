package com.illudtechzone.booking.domain;



import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.illudtechzone.booking.domain.enumeration.RequestStatus;

import com.illudtechzone.booking.domain.enumeration.RideStatus;

/**
 * A Ride.
 */
@Entity
@Table(name = "ride")
@Document(indexName = "ride")
public class Ride implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "distance")
    private Long distance;

    @Column(name = "pickup_address")
    private String pickupAddress;

    @Column(name = "pickup_place_id")
    private String pickupPlaceId;

    @Column(name = "destination_place_id")
    private String destinationPlaceId;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "pickup_geopoint")
    private String pickupGeopoint;

    @Column(name = "destination_geopoint")
    private String destinationGeopoint;

    @Column(name = "customer_id")
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "requested_status")
    private RequestStatus requestedStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "accepted_status")
    private RideStatus acceptedStatus;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "jhi_cost")
    private Double cost;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "tracking_id")
    private String trackingId;

    @OneToOne
    @JoinColumn(unique = true)
    private Invoice invoice;

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

    public Ride distance(Long distance) {
        this.distance = distance;
        return this;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public Ride pickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
        return this;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupPlaceId() {
        return pickupPlaceId;
    }

    public Ride pickupPlaceId(String pickupPlaceId) {
        this.pickupPlaceId = pickupPlaceId;
        return this;
    }

    public void setPickupPlaceId(String pickupPlaceId) {
        this.pickupPlaceId = pickupPlaceId;
    }

    public String getDestinationPlaceId() {
        return destinationPlaceId;
    }

    public Ride destinationPlaceId(String destinationPlaceId) {
        this.destinationPlaceId = destinationPlaceId;
        return this;
    }

    public void setDestinationPlaceId(String destinationPlaceId) {
        this.destinationPlaceId = destinationPlaceId;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public Ride destinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
        return this;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getPickupGeopoint() {
        return pickupGeopoint;
    }

    public Ride pickupGeopoint(String pickupGeopoint) {
        this.pickupGeopoint = pickupGeopoint;
        return this;
    }

    public void setPickupGeopoint(String pickupGeopoint) {
        this.pickupGeopoint = pickupGeopoint;
    }

    public String getDestinationGeopoint() {
        return destinationGeopoint;
    }

    public Ride destinationGeopoint(String destinationGeopoint) {
        this.destinationGeopoint = destinationGeopoint;
        return this;
    }

    public void setDestinationGeopoint(String destinationGeopoint) {
        this.destinationGeopoint = destinationGeopoint;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Ride customerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public RequestStatus getRequestedStatus() {
        return requestedStatus;
    }

    public Ride requestedStatus(RequestStatus requestedStatus) {
        this.requestedStatus = requestedStatus;
        return this;
    }

    public void setRequestedStatus(RequestStatus requestedStatus) {
        this.requestedStatus = requestedStatus;
    }

    public RideStatus getAcceptedStatus() {
        return acceptedStatus;
    }

    public Ride acceptedStatus(RideStatus acceptedStatus) {
        this.acceptedStatus = acceptedStatus;
        return this;
    }

    public void setAcceptedStatus(RideStatus acceptedStatus) {
        this.acceptedStatus = acceptedStatus;
    }

    public Long getDriverId() {
        return driverId;
    }

    public Ride driverId(Long driverId) {
        this.driverId = driverId;
        return this;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Double getCost() {
        return cost;
    }

    public Ride cost(Double cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public Ride createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Ride trackingId(String trackingId) {
        this.trackingId = trackingId;
        return this;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Ride invoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
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
        Ride ride = (Ride) o;
        if (ride.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ride.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ride{" +
            "id=" + getId() +
            ", distance=" + getDistance() +
            ", pickupAddress='" + getPickupAddress() + "'" +
            ", pickupPlaceId='" + getPickupPlaceId() + "'" +
            ", destinationPlaceId='" + getDestinationPlaceId() + "'" +
            ", destinationAddress='" + getDestinationAddress() + "'" +
            ", pickupGeopoint='" + getPickupGeopoint() + "'" +
            ", destinationGeopoint='" + getDestinationGeopoint() + "'" +
            ", customerId=" + getCustomerId() +
            ", requestedStatus='" + getRequestedStatus() + "'" +
            ", acceptedStatus='" + getAcceptedStatus() + "'" +
            ", driverId=" + getDriverId() +
            ", cost=" + getCost() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", trackingId='" + getTrackingId() + "'" +
            "}";
    }
}
