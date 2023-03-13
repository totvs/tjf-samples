package com.tjf.sample.github.tenantdiscriminator.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import com.totvs.tjf.tenant.discriminator.TenantId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class HabitantModelId extends TenantId {

	private static final long serialVersionUID = 4010942799108332905L;

	@NotNull
	private String id;

}
