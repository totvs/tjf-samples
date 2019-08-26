package com.tjf.sample.github.validation.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}
