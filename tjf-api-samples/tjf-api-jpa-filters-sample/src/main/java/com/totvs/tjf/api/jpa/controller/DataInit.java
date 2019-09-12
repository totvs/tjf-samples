package com.totvs.tjf.api.jpa.controller;

import java.math.BigDecimal;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.totvs.tjf.api.jpa.model.AccountModel;
import com.totvs.tjf.api.jpa.model.AccountModelRepository;
import com.totvs.tjf.api.jpa.model.EmployeeModel;
import com.totvs.tjf.api.jpa.model.EmployeeModelRepository;

@Component
public class DataInit {

	@Autowired
	private EmployeeModelRepository employeeRepos;

	@Autowired
	private AccountModelRepository accountRepos;
	
	@PostConstruct
	@Transactional
	private void init () {
		EmployeeModel john = new EmployeeModel();
		john.setEmployeeId(UUID.randomUUID().toString());
		john.setName("John");
		employeeRepos.saveAndFlush(john);
		EmployeeModel mary = new EmployeeModel();
		mary.setEmployeeId(UUID.randomUUID().toString());
		mary.setName("Mary");
		employeeRepos.saveAndFlush(mary);
		boolean isJohn = true;
		for (int i=0; i < 10; i++) {
			AccountModel account = new AccountModel();
			account.setAccountId(UUID.randomUUID().toString());
			account.setBalance(new BigDecimal(Math.random() * 10000));
			account.setBalanceCurrencyCode("BRL");
			account.setLimit(new BigDecimal(10000 + i * 1000));
			account.setLimitCurrencyCode("BRL");
			if (isJohn = !isJohn) {
				account.setEmployee(mary);
			}
			else {
				account.setEmployee(john);
			}
			accountRepos.saveAndFlush(account);
		}
	}
	
}