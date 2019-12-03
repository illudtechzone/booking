package com.illudtechzone.booking.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import com.illudtechzone.booking.domain.enumeration.RequestStatus;
import com.illudtechzone.booking.domain.enumeration.RideStatus;

/**
 * A DTO for the Ride entity.
 */
public class RideDTO implements Serializable {

    private Long id;

    private Long distance;

    private String pickupAddress;

    private String pickupPlaceId;

    private String destinationPlaceId;

    private String destinationAddress;

    private String pickupGeopoint;

    private String destinationGeopoint;

    private Long customerId;

    private RequestStatus requestedStatus;

    private RideStatus acceptedStatus;

    private Long driverId;

    private Double cost;

    private Instant createdTime;

    private String trackingId;


    private Long invoiceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupPlaceId() {
        return pickupPlaceId;
    }

    public void setPickupPlaceId(String pickupPlaceId) {
        this.pickupPlaceId = pickupPlaceId;
    }

    public String getDestinationPlaceId() {
        return destinationPlaceId;
    }

    public void setDestinationPlaceId(String destinationPlaceId) {
        this.destinationPlaceId = destinationPlaceId;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getPickupGeopoint() {
        return pickupGeopoint;
    }

    public void setPickupGeopoint(String pickupGeopoint) {
        this.pickupGeopoint = pickupGeopoint;
    }

    public String getDestinationGeopoint() {
        return destinationGeopoint;
    }

    public void setDestinationGeopoint(String destinationGeopoint) {
        this.destinationGeopoint = destinationGeopoint;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public RequestStatus getRequestedStatus() {
        return requestedStatus;
    }

    public void setRequestedStatus(RequestStatus requestedStatus) {
        this.requestedStatus = requestedStatus;
    }

    public RideStatus getAcceptedStatus() {
        return acceptedStatus;
    }

    public void setAcceptedStatus(RideStatus acceptedStatus) {
        this.acceptedStatus = acceptedStatus;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RideDTO rideDTO = (RideDTO) o;
        if (rideDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rideDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RideDTO{" +
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
            ", invoice=" + getInvoiceId() +
            "}";
    }
}
