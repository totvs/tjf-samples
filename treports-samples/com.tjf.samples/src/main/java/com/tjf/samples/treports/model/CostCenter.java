package com.tjf.samples.treports.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class CostCenter {

	@Id
	String code;
	String companyId;
	Situation registerSituation;
	String name;
	String shortCode;
	boolean sped;

	public CostCenter() {
	}

	public CostCenter(String companyId, String code, Situation registerSituation, String name, String shortCode,
			boolean sped) {
		this.companyId = companyId;
		this.code = code;
		this.registerSituation = registerSituation;
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
	public Situation getRegisterSituation() {
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

		public String situation;

		Situation(String situation) {
			this.situation = situation;
		}
	}
}