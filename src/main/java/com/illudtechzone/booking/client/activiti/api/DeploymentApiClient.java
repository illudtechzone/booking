package com.illudtechzone.booking.client.activiti.api;

import org.springframework.cloud.openfeign.FeignClient;
import com.illudtechzone.booking.client.activiti.ClientConfiguration;

@FeignClient(name="${activiti.name:activiti}", url="${activiti.url:http://localhost:8080/activiti-rest/service}", configuration = ClientConfiguration.class)
public interface DeploymentApiClient extends DeploymentApi {
}