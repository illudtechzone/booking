package com.illudtechzone.booking.service.mapper;

import com.illudtechzone.booking.domain.*;
import com.illudtechzone.booking.service.dto.RideDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Ride and its DTO RideDTO.
 */
@Mapper(componentModel = "spring", uses = {InvoiceMapper.class})
public interface RideMapper extends EntityMapper<RideDTO, Ride> {

    @Mapping(source = "invoice.id", target = "invoiceId")
    RideDTO toDto(Ride ride);

    @Mapping(source = "invoiceId", target = "invoice")
    Ride toEntity(RideDTO rideDTO);

    default Ride fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ride ride = new Ride();
        ride.setId(id);
        return ride;
    }
}
