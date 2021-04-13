package com.tjf.sample.github.api.r2dbc.model;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table("cash_employee")
public class EmployeeModel /* implements SimpleFilterSupport<EmployeeModel> */ {

	private static final long serialVersionUID = -958964907040473218L;

	@Id
	private Integer id;

	@NotNull
	private String name;
}