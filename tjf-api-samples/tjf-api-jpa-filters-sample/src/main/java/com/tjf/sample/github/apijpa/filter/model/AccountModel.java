package com.tjf.sample.github.apijpa.filter.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupport;
import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupportAllow;
import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupportDeny;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cash_account")
@SimpleFilterSupportDeny
public class AccountModel implements SimpleFilterSupport<AccountModel> {

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

}
