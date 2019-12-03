package com.illudtechzone.booking.service;

import org.springframework.http.ResponseEntity;

import com.illudtechzone.booking.client.activiti.model.ProcessInstanceResponse;
import com.illudtechzone.booking.client.activiti.model.custom.CompleteRide;
import com.illudtechzone.booking.client.activiti.model.custom.DriverResponse;
import com.illudtechzone.booking.client.activiti.model.custom.Payment;
import com.illudtechzone.booking.client.activiti.model.custom.RiderLocationDetails;
import com.illudtechzone.booking.client.activiti.model.custom.StartRide;

public interface BookingCommandService {
	String initiate();

	ResponseEntity<ProcessInstanceResponse> collectRiderLocationDetails(String taskId, RiderLocationDetails riderLocationDetails);

	ResponseEntity<ProcessInstanceResponse> sentResponse(String taskId,DriverResponse driverResponse);


	ResponseEntity<ProcessInstanceResponse> startRide(String taskId, StartRide startRide);

	ResponseEntity<ProcessInstanceResponse> completeRide(String taskId, CompleteRide completeRide);

	ResponseEntity<ProcessInstanceResponse> payment(String taskId, Payment payment);

}
