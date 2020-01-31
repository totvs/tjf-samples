package com.tjf.sample.github.validation.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountModel {

	@NotNull
	private Integer accountId;

	@NotNull(message = "Nome não pode ser nulo!")
	@Size(min = 4, max = 40)
	private String name;

	@Size(min = 10, max = 100)
	private String address;

	@NotNull
	@Min(value = 10, message = "Valor minimo é 10!")
	@Max(value = 1000, message = "Valor maximo é 1000!")
	private Double balance;

	public AccountModel() {
		super();
	}

	public AccountModel(Integer accountId, String name, String address, Double balance) {
		super();
		this.accountId = accountId;
		this.name = name;
		this.address = address;
		this.balance = balance;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
}
