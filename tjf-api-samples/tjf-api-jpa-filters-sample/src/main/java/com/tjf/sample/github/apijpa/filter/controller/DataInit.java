package com.tjf.sample.github.apijpa.filter.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tjf.sample.github.apijpa.filter.model.AccountModel;
import com.tjf.sample.github.apijpa.filter.model.AccountModelRepository;
import com.tjf.sample.github.apijpa.filter.model.EmployeeModel;
import com.tjf.sample.github.apijpa.filter.model.EmployeeModelRepository;

@Component
public class DataInit {

	@Autowired
	private EmployeeModelRepository employeeRepos;

	@Autowired
	private AccountModelRepository accountRepos;

	private List<AccountModel> accounts = new ArrayList<>();

	@PostConstruct
	@Transactional
	private void init() {
		EmployeeModel john = new EmployeeModel();
		john.setEmployeeId(1);
		john.setName("John");
		employeeRepos.saveAndFlush(john);

		EmployeeModel mary = new EmployeeModel();
		mary.setEmployeeId(2);
		mary.setName("Mary");
		employeeRepos.saveAndFlush(mary);

		boolean isJohn = true;

		for (int i = 0; i < 10; i++) {
			AccountModel account = new AccountModel();
			account.setAccountId(i);

			if (i != 8) {
				account.setBalance(new BigDecimal(i * 1000));
			} else {
				account.setBalance(new BigDecimal(9 * 1000));
			}

			account.setBalanceCurrencyCode("BRL");
			account.setLimit(new BigDecimal(10000 + i * 1000));
			account.setLimitCurrencyCode("BRL");

			if (isJohn = !isJohn) {
				account.setEmployee(mary);
			} else {
				account.setEmployee(john);
			}
			
			accountRepos.saveAndFlush(account);
			accounts.add(account);
		}
	}

	public List<AccountModel> getAccounts() {
		return accounts;
	}
	
}
