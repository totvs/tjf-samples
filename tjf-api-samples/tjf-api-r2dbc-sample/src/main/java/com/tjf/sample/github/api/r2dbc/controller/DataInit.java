package com.tjf.sample.github.api.r2dbc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tjf.sample.github.api.r2dbc.model.AccountModel;
import com.tjf.sample.github.api.r2dbc.model.AccountModelRepository;
import com.tjf.sample.github.api.r2dbc.model.EmployeeModel;
import com.tjf.sample.github.api.r2dbc.model.EmployeeModelRepository;

import reactor.core.publisher.Mono;

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
		john.setName("John");
		Mono<EmployeeModel> monoJohn = employeeRepos.save(john);
		
		monoJohn.subscribe(value -> System.out.println("John id: " + value.getId()));

		EmployeeModel mary = new EmployeeModel();
		mary.setName("Mary");
		Mono<EmployeeModel> monoMary = employeeRepos.save(mary);

		monoMary.subscribe(value -> System.out.println("Mary id: " + value.getId()));
		
		/*boolean isJohn = true;

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
			
			accountRepos.save(account);
			accounts.add(account);
		}*/
	}

	public List<AccountModel> getAccounts() {
		return accounts;
	}

}
