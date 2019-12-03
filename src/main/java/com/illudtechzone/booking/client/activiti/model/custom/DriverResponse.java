package com.illudtechzone.booking.client.activiti.model.custom;

public class DriverResponse {
	
	String status;
	String email;
	String trakingId;
	String name;
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}
	
	public String getTrakingId() {
		return trakingId;
	}
	public void setTrakingId(String trakingId) {
		this.trakingId = trakingId;
	}
	public void setName(String name) {
		this.name = name;
	}

}
