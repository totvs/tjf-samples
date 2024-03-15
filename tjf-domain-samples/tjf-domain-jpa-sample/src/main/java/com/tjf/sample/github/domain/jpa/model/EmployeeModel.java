package com.tjf.sample.github.domain.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupport;

import lombok.Getter;
@Getter
@Entity
@Table(name = "cash_employee")
public class EmployeeModel implements SimpleFilterSupport<EmployeeModel> {

	private static final long serialVersionUID = -958964907040473218L;

	@Id
	@NotNull
	private Integer employeeId;

	@NotNull
	@Column(name = "name")
	private String name;

	@JsonCreator
	public EmployeeModel(@JsonProperty("employeeId") @NotNull Integer employeeId,
			@JsonProperty("name") @NotNull String name) {
		this.employeeId = employeeId;
		this.name = name;
	}

	public EmployeeModel() {
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public String getName() {
		return name;
	}

}