package com.illudtechzone.booking.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.illudtechzone.booking.client.activiti.model.ProcessInstanceResponse;
import com.illudtechzone.booking.client.activiti.model.custom.CompleteRide;
import com.illudtechzone.booking.client.activiti.model.custom.DriverResponse;
import com.illudtechzone.booking.client.activiti.model.custom.Payment;
import com.illudtechzone.booking.client.activiti.model.custom.RiderLocationDetails;
import com.illudtechzone.booking.client.activiti.model.custom.StartRide;
import com.illudtechzone.booking.service.BookingCommandService;
@RestController
@RequestMapping("/api/command")
public class BookingCommandResource {
	
	@Autowired
	BookingCommandService bookingCommandService;
	
	   @PostMapping("/initiate") 
		  public String initateWorkflow() {
		  
			
		   	return bookingCommandService.initiate();
		  
		  }
	    
		@PostMapping("/collectRiderLocationDetails/{taskId}")
		public ResponseEntity<ProcessInstanceResponse> collectRiderLocationDetails(@PathVariable String taskId,@RequestBody RiderLocationDetails riderLocationDetails ) {
			return bookingCommandService.collectRiderLocationDetails(taskId,riderLocationDetails);
		}
		
		@PostMapping("/sentResponse/{taskId}")
		public ResponseEntity<ProcessInstanceResponse> sentResponse(@PathVariable String taskId,@RequestBody DriverResponse driverResponse){
			
			return 	this.bookingCommandService.sentResponse(taskId,driverResponse);

		}
		
		@PostMapping("/startRide/{taskId}")
		public ResponseEntity<ProcessInstanceResponse> startRide(@PathVariable String taskId,@RequestBody StartRide startRide){
			
			return 	this.bookingCommandService.startRide(taskId,startRide);

		}
		
		@PostMapping("/completeRide/{taskId}")
		public ResponseEntity<ProcessInstanceResponse> completedRide(@PathVariable String taskId,@RequestBody CompleteRide completeRide){
			
			return 	this.bookingCommandService.completeRide(taskId,completeRide);

		}
		
	
		@PostMapping("/payment/{taskId}")
		public ResponseEntity<ProcessInstanceResponse> payment(@PathVariable String taskId,@RequestBody  Payment payment){
			
			return 	this.bookingCommandService.payment(taskId,payment);

		}
		
}
