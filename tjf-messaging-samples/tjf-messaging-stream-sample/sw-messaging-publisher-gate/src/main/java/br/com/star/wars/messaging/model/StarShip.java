package br.com.star.wars.messaging.model;

import com.totvs.tjf.messaging.Tenantable;

public class StarShip implements Tenantable {

	private String name;
	private String tenantId;

	public StarShip(String name) {
		this.name = name;
	}

	public StarShip() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	@Override
	public String getTenantId() {
		return this.tenantId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}