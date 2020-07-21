package com.tjf.sample.github.apijpa.filter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}