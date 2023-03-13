package com.tjf.sample.github.corevalidation.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel {

	@NotNull
	private int accountId;

	@NotNull(message = "Nome não pode ser nulo!")
	@Size(min = 4, max = 40)
	private String name;

	@Size(min = 10, max = 100)
	private String address;

	@NotNull
	@Min(value = 10, message = "Valor minimo é 10!")
	@Max(value = 1000, message = "Valor maximo é 1000!")
	private Double balance;

	

}
