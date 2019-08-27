package br.com.star.wars.habitants.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import com.totvs.tjf.tenant.discriminator.TenantId;

@Embeddable
public class HabitantModelId extends TenantId {

	private static final long serialVersionUID = 4010942799108332905L;

	@NotNull
	private String id;

	public HabitantModelId() {}

	public HabitantModelId(String id) {
		this.setId(id);
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
