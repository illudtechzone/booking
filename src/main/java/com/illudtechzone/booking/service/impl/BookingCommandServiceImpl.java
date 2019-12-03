package com.illudtechzone.booking.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.illudtechzone.booking.client.activiti.api.FormsApi;
import com.illudtechzone.booking.client.activiti.api.ProcessInstancesApi;
import com.illudtechzone.booking.client.activiti.model.ProcessInstanceCreateRequest;
import com.illudtechzone.booking.client.activiti.model.ProcessInstanceResponse;
import com.illudtechzone.booking.client.activiti.model.RestFormProperty;
import com.illudtechzone.booking.client.activiti.model.RestVariable;
import com.illudtechzone.booking.client.activiti.model.SubmitFormRequest;
import com.illudtechzone.booking.client.activiti.model.custom.CompleteRide;
import com.illudtechzone.booking.client.activiti.model.custom.DriverResponse;
import com.illudtechzone.booking.client.activiti.model.custom.Payment;
import com.illudtechzone.booking.client.activiti.model.custom.RiderLocationDetails;
import com.illudtechzone.booking.client.activiti.model.custom.StartRide;
import com.illudtechzone.booking.service.BookingCommandService;

@Service
public class BookingCommandServiceImpl  implements BookingCommandService{
	
    private final Logger log = LoggerFactory.getLogger(RideServiceImpl.class);

	
	@Autowired
    private FormsApi formsApi;
	
    @Autowired
    private ProcessInstancesApi processInstanceApi;
    
	
	@Override
	public String initiate() {

		
			
			ProcessInstanceCreateRequest processInstanceCreateRequest=new ProcessInstanceCreateRequest();
	   		List<RestVariable> variables=new ArrayList<RestVariable>();
	   		
	   		//processInstanceCreateRequest.setProcessDefinitionId("illuid-work:1:2504");
	   		//processInstanceCreateRequest.setProcessDefinitionId("illuid-work:3:10814");
	   		//processInstanceCreateRequest.setProcessDefinitionId("illuid-work:4:11267");
	   		processInstanceCreateRequest.setProcessDefinitionId("illuid-work:1:43");
	   		
	   		RestVariable riderRestVariable=new RestVariable();
	   		riderRestVariable.setName("rider");
	   		riderRestVariable.setType("string");
	   		riderRestVariable.setValue("rider");
	   		variables.add(riderRestVariable);
	   		
	   		RestVariable driverRestVariable=new RestVariable();
	   		driverRestVariable.setName("driver");
	   		driverRestVariable.setType("string");
	   		driverRestVariable.setValue("driver");
	   		
	   		variables.add(driverRestVariable);
	   	
	   		
	   		
	   		log.info("*****************************************************"+variables.size());
	   		processInstanceCreateRequest.setVariables(variables);
	   		log.info("*****************************************************"+processInstanceCreateRequest.getVariables());
	   		
	   		ResponseEntity<ProcessInstanceResponse> processInstanceResponse = processInstanceApi
					.createProcessInstance(processInstanceCreateRequest);
			String processInstanceId = processInstanceResponse.getBody().getId();
			String processDefinitionId = processInstanceCreateRequest.getProcessDefinitionId();
			log.info("++++++++++++++++processDefinitionId++++++++++++++++++"+ processDefinitionId);
			log.info("++++++++++++++++ProcessInstanceId is+++++++++++++ " + processInstanceId);
			
	   		processInstanceApi.createProcessInstance(processInstanceCreateRequest);
	   		
			
			return processInstanceId;
			
		}

	@Override
	public ResponseEntity<ProcessInstanceResponse> collectRiderLocationDetails(String taskId, RiderLocationDetails riderLocationDetails) {

		List<RestFormProperty> formProperties =new ArrayList<RestFormProperty>();
		SubmitFormRequest submitForm=new SubmitFormRequest();
		submitForm.setAction("completed");
		submitForm.setTaskId(taskId);
		
		RestFormProperty distanceForm =new RestFormProperty();
		distanceForm.setId("distance");
		distanceForm.setType("String");
		distanceForm.setName("distance");
		distanceForm.setReadable(true);
		distanceForm.setValue(riderLocationDetails.getDistance());
		formProperties.add(distanceForm);
		
		RestFormProperty pickUpForm =new RestFormProperty();
		pickUpForm.setId("pickUp");
		pickUpForm.setType("String");
		pickUpForm.setName("pickUp");
		pickUpForm.setReadable(true);
		pickUpForm.setValue(riderLocationDetails.getPickUp());
		formProperties.add(pickUpForm);
		
		
		RestFormProperty destinationForm =new RestFormProperty();
		destinationForm.setId("destination");
		destinationForm.setType("String");
		destinationForm.setName("destination");
		destinationForm.setReadable(true);
		destinationForm.setValue(riderLocationDetails.getDestination());
		formProperties.add(destinationForm);
		
		
		
		submitForm.setProperties(formProperties);
		
		return formsApi.submitForm(submitForm);
		
		
	}

	@Override
	public ResponseEntity<ProcessInstanceResponse> sentResponse(String taskId,DriverResponse driverResponse) {
		List<RestFormProperty> formProperties =new ArrayList<RestFormProperty>();
		SubmitFormRequest submitForm=new SubmitFormRequest();
		submitForm.setAction("completed");
		submitForm.setTaskId(taskId);
		
		RestFormProperty nameFormProperty = new RestFormProperty();
   		nameFormProperty.setId("name");
   		nameFormProperty.setName("name");
   		nameFormProperty.setType("String");
   		nameFormProperty.setReadable(true);
   		nameFormProperty.setValue(driverResponse.getName());
   		formProperties.add(nameFormProperty);
   		
   		RestFormProperty emailFormProperty = new RestFormProperty();
   		emailFormProperty.setId("email");
   		emailFormProperty.setName("email");
   		emailFormProperty.setType("String");
   		emailFormProperty.setReadable(true);
   		emailFormProperty.setValue(driverResponse.getEmail());
   		formProperties.add(emailFormProperty);
   		
   		
   		RestFormProperty statusFormProperty = new RestFormProperty();
   		statusFormProperty.setId("status");
   		statusFormProperty.setName("status");
   		statusFormProperty.setType("String");
   		statusFormProperty.setReadable(true);
   		statusFormProperty.setValue(driverResponse.getStatus());
   		formProperties.add(statusFormProperty);
   		
   		RestFormProperty trackingIdFormProperty = new RestFormProperty();
   		trackingIdFormProperty.setId("trackingId");
   		trackingIdFormProperty.setName("trackingId");
   		trackingIdFormProperty.setType("String");
   		trackingIdFormProperty.setReadable(true);
   		trackingIdFormProperty.setValue(driverResponse.getTrakingId());
   		formProperties.add(trackingIdFormProperty);
   		
   		
   		
   		submitForm.setProperties(formProperties);
   		
   		log.debug("form################"+formProperties);

   		return formsApi.submitForm(submitForm);
		
	
	
	}

	@Override
	public ResponseEntity<ProcessInstanceResponse> startRide(String taskId, StartRide startRide) {
		
		List<RestFormProperty> formProperties =new ArrayList<RestFormProperty>();
		SubmitFormRequest submitForm=new SubmitFormRequest();
		submitForm.setAction("completed");
		submitForm.setTaskId(taskId);
		
		RestFormProperty statusForm=new RestFormProperty();
		statusForm.setId("status");
		statusForm.setName("status");
		statusForm.setType("String");
		statusForm.setReadable(true);
		statusForm.setValue(startRide.getStatus());
   		formProperties.add(statusForm);
   		
   		RestFormProperty trackingIdFormProperty = new RestFormProperty();
   		trackingIdFormProperty.setId("trackingId");
   		trackingIdFormProperty.setName("trackingId");
   		trackingIdFormProperty.setType("String");
   		trackingIdFormProperty.setReadable(true);
   		trackingIdFormProperty.setValue(startRide.getTrakingId());
   		formProperties.add(trackingIdFormProperty);
   		
   		
   		submitForm.setProperties(formProperties);
   		
   		log.debug("form################"+formProperties);

   		return formsApi.submitForm(submitForm);
		
	}

	@Override
	public ResponseEntity<ProcessInstanceResponse> completeRide(String taskId, CompleteRide completeRide) {
	
		List<RestFormProperty> formProperties =new ArrayList<RestFormProperty>();
		SubmitFormRequest submitForm=new SubmitFormRequest();
		submitForm.setAction("completed");
		submitForm.setTaskId(taskId);
		
		RestFormProperty statusForm=new RestFormProperty();
		statusForm.setId("status");
		statusForm.setName("status");
		statusForm.setType("String");
		statusForm.setReadable(true);
		statusForm.setValue(completeRide.getStatus());
   		formProperties.add(statusForm);
   		
   		RestFormProperty trackingIdFormProperty = new RestFormProperty();
   		trackingIdFormProperty.setId("trackingId");
   		trackingIdFormProperty.setName("trackingId");
   		trackingIdFormProperty.setType("String");
   		trackingIdFormProperty.setReadable(true);
   		trackingIdFormProperty.setValue(completeRide.getTrakingId());
   		formProperties.add(trackingIdFormProperty);
   		
   		
   		submitForm.setProperties(formProperties);
   		
   		log.debug("form################"+formProperties);

   		return formsApi.submitForm(submitForm);
	}

	@Override
	public ResponseEntity<ProcessInstanceResponse> payment(String taskId, Payment payment) {

		List<RestFormProperty> formProperties =new ArrayList<RestFormProperty>();
		SubmitFormRequest submitForm=new SubmitFormRequest();
		submitForm.setAction("completed");
		submitForm.setTaskId(taskId);
		
		RestFormProperty paymentStatusForm=new RestFormProperty();
		paymentStatusForm.setId("paymentStatus");
		paymentStatusForm.setName("paymentStatus");
		paymentStatusForm.setType("String");
		paymentStatusForm.setReadable(true);
		paymentStatusForm.setValue(payment.getPaymentStatus());
   		formProperties.add(paymentStatusForm);
   		
   		RestFormProperty trackingIdFormProperty = new RestFormProperty();
   		trackingIdFormProperty.setId("trackingId");
   		trackingIdFormProperty.setName("trackingId");
   		trackingIdFormProperty.setType("String");
   		trackingIdFormProperty.setReadable(true);
   		trackingIdFormProperty.setValue(payment.getTrakingId());
   		formProperties.add(trackingIdFormProperty);
   		
   		
   		submitForm.setProperties(formProperties);
   		
   		log.debug("form################"+formProperties);

   		return formsApi.submitForm(submitForm);
	}
	

}
