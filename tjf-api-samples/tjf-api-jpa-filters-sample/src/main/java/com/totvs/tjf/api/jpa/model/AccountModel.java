package com.totvs.tjf.api.jpa.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupport;
import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupportAllow;
import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupportDeny;

@Entity
@Table(name = "cash_account")
@SimpleFilterSupportDeny
public class AccountModel implements SimpleFilterSupport <AccountModel> {

	private static final long serialVersionUID = 2975307395809820307L;

	@Id
    @NotNull
    private Integer accountId;

    @NotNull
    @Column(name = "val_balance")
    private BigDecimal balance;

    @NotNull
    @Column(name = "cod_balance_currency")
    private String balanceCurrencyCode;

    @NotNull
    @Column(name = "val_limit")
    private BigDecimal limit;

    @NotNull
    @Column(name = "cod_limit_currency")
    @SimpleFilterSupportAllow
    private String limitCurrencyCode;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id")
    private EmployeeModel employee;

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getBalanceCurrencyCode() {
		return balanceCurrencyCode;
	}
	
	public void setBalanceCurrencyCode(String balanceCurrencyCode) {
		this.balanceCurrencyCode = balanceCurrencyCode;
	}

	public BigDecimal getLimit() {
		return limit;
	}

	public void setLimit(BigDecimal limit) {
		this.limit = limit;
	}

	public String getLimitCurrencyCode() {
		return limitCurrencyCode;
	}

	public void setLimitCurrencyCode(String limitCurrencyCode) {
		this.limitCurrencyCode = limitCurrencyCode;
	}

	public EmployeeModel getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeModel employee) {
		this.employee = employee;
	}

}
