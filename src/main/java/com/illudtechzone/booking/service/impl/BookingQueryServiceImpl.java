package com.illudtechzone.booking.service.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.illudtechzone.booking.client.activiti.api.TasksApi;
import com.illudtechzone.booking.client.activiti.model.DataResponse;
import com.illudtechzone.booking.service.BookingQueryService;


@Service
public class BookingQueryServiceImpl implements BookingQueryService{

	@Autowired
	TasksApi tasksApi;
	public ResponseEntity<DataResponse> getTasks(String name, String nameLike, String description, String priority,
			String minimumPriority, String maximumPriority, String assignee, String assigneeLike, String owner,
			String ownerLike, String unassigned, String delegationState, String candidateUser, String candidateGroup,
			String candidateGroups, String involvedUser, String taskDefinitionKey, String taskDefinitionKeyLike,
			String processInstanceId, String processInstanceBusinessKey, String processInstanceBusinessKeyLike,
			String processDefinitionId, String processDefinitionKey, String processDefinitionKeyLike,
			String processDefinitionName, String processDefinitionNameLike, String executionId, String createdOn,
			String createdBefore, String createdAfter, String dueOn, String dueBefore, String dueAfter,
			Boolean withoutDueDate, Boolean excludeSubTasks, Boolean active, Boolean includeTaskLocalVariables,
			Boolean includeProcessVariables, String tenantId, String tenantIdLike, Boolean withoutTenantId,
			String candidateOrAssigned, String category) {
		
		return tasksApi.getTasks(name, nameLike, description, priority, minimumPriority, maximumPriority, assignee, assigneeLike, owner, ownerLike, unassigned, delegationState, candidateUser, candidateGroup, candidateGroups, involvedUser, taskDefinitionKey, taskDefinitionKeyLike, processInstanceId, processInstanceBusinessKey, processInstanceBusinessKeyLike, processDefinitionId, processDefinitionKey, processDefinitionKeyLike, processDefinitionName, processDefinitionNameLike, executionId, createdOn, createdBefore, createdAfter, dueOn, dueBefore, dueAfter, withoutDueDate, excludeSubTasks, active, includeTaskLocalVariables, includeProcessVariables, tenantId, tenantIdLike, withoutTenantId, candidateOrAssigned, category);
	}

}
