package com.tjf.sample.github.coremoney.model;

import java.util.UUID;

import javax.money.MonetaryAmount;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel {

	@NotNull
	private UUID accountId;
	private MonetaryAmount price;
}
