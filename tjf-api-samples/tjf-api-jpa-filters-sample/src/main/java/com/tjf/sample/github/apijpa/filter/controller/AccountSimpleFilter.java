package com.tjf.sample.github.apijpa.filter.controller;

import java.math.BigDecimal;

import com.tjf.sample.github.apijpa.filter.model.AccountModel;
import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSimpleFilter implements SimpleFilterSupport<AccountModel> {

	private static final long serialVersionUID = 5118512538332202507L;

	private BigDecimal limit;
	private Employee employee = new Employee();

	@Getter
	@Setter
	public class Employee {
		private String name;
	}

}