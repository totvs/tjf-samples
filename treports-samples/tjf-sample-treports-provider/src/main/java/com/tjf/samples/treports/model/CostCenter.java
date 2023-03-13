package com.tjf.samples.treports.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class CostCenter {

	@Id
	private String code;
	private String companyId;
	private String registerSituation;
	private String name;
	private String shortCode;
	private boolean sped;

	public CostCenter() {
	}

	public CostCenter(String companyId, String code, Situation situation, String name, String shortCode,
			boolean sped) {
		this.companyId = companyId;
		this.code = code;
		this.registerSituation = situation.value;
		this.name = name;
		this.shortCode = shortCode;
		this.sped = sped;
	}

	@JsonProperty("CompanyId")
	public String getCompanyId() {
		return companyId;
	}

	@JsonProperty("Code")
	public String getCode() {
		return code;
	}

	@JsonProperty("RegisterSituation")
	public String getRegisterSituation() {
		return registerSituation;
	}

	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	@JsonProperty("ShortCode")
	public String getShortCode() {
		return shortCode;
	}

	@JsonProperty("SPED")
	public boolean isSped() {
		return sped;
	}

	public enum Situation {
		ACTIVE("active"), INACTIVE("Inactive");

		public String value;

		Situation(String value) {
			this.value = value;
		}
	}
}