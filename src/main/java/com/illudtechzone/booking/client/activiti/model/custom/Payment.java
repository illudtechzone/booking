package com.illudtechzone.booking.client.activiti.model.custom;

public class Payment {
	
	String TrakingId;
	public String getTrakingId() {
		return TrakingId;
	}
	public void setTrakingId(String trakingId) {
		TrakingId = trakingId;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	String paymentStatus;


}
