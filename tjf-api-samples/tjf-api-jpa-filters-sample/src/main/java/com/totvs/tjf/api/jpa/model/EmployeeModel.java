package com.totvs.tjf.api.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupport;

@Entity
@Table(name = "cash_employee")
public class EmployeeModel implements SimpleFilterSupport <EmployeeModel> {

	private static final long serialVersionUID = -958964907040473218L;

	@Id
    @NotNull
    private Integer employeeId;

    @NotNull
    @Column(name = "name")
    private String name;
    
	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}