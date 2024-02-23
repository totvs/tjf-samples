package com.tjf.sample.github.domain.jpa.model;

import java.math.BigDecimal;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.totvs.tjf.api.jpa.simplefilter.SimpleFilterSupportAllow;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cash_account")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountModel implements Persistable<AccountId> {

	@Id
	@JsonIgnoreProperties
	private AccountId id;

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

	//@NotNull
	//@ManyToOne
	//@JoinColumn(name = "id")
	//private EmployeeModel employee;

	@Override
	public boolean isNew() {
		return false;
	}
}
