package com.totvs.tjf.api.jpa.controller;

import java.math.BigDecimal;

import com.totvs.tjf.api.jpa.model.AccountModel;
import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupport;

public class AccountSimpleFilter implements SimpleFilterSupport <AccountModel> {

	private static final long serialVersionUID = 5118512538332202507L;
	
	public BigDecimal limit;
	public Employee employee = new Employee();
	
	public AccountSimpleFilter () {}

	public BigDecimal getLimit() {
		return limit;
	}

	public void setLimit(BigDecimal limit) {
		this.limit = limit;
	}
	
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public class Employee {
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
}