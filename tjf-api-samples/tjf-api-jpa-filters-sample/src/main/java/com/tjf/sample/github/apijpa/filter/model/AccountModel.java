package com.tjf.sample.github.apijpa.filter.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tjf.sample.github.apijpa.filter.adapter.ZonedDateTimeXmlAdapter;
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

	@NotNull
	@Column(name = "created")
	private ZonedDateTime created = ZonedDateTime.now();

	@XmlJavaTypeAdapter(value = ZonedDateTimeXmlAdapter.class)
	public ZonedDateTime getCreated() {
		return created;
	}

}
