package com.tjf.samples.treports.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class RetailStockLevel {

	@Id
	private String id;
	private String companyId;
	private String branchId;
	private String itemInternalId;
	private String warehouseInternalID;
	private long currentStockAmount;
	private long bookedStockAmount;
	private long futureStockAmount;
	private long valueofCurrentStockAmount;

	public RetailStockLevel() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("CompanyId")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@JsonProperty("BranchId")
	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	@JsonProperty("ItemInternalId")
	public String getItemInternalId() {
		return itemInternalId;
	}

	public void setItemInternalId(String itemInternalId) {
		this.itemInternalId = itemInternalId;
	}

	@JsonProperty("WarehouseInternalID")
	public String getWarehouseInternalID() {
		return warehouseInternalID;
	}

	public void setWarehouseInternalID(String warehouseInternalID) {
		this.warehouseInternalID = warehouseInternalID;
	}

	@JsonProperty("CurrentStockAmount")
	public long getCurrentStockAmount() {
		return currentStockAmount;
	}

	public void setCurrentStockAmount(long currentStockAmount) {
		this.currentStockAmount = currentStockAmount;
	}

	@JsonProperty("BookedStockAmount")
	public long getBookedStockAmount() {
		return bookedStockAmount;
	}

	public void setBookedStockAmount(long bookedStockAmount) {
		this.bookedStockAmount = bookedStockAmount;
	}

	@JsonProperty("FutureStockAmount")
	public long getFutureStockAmount() {
		return futureStockAmount;
	}

	public void setFutureStockAmount(long futureStockAmount) {
		this.futureStockAmount = futureStockAmount;
	}

	@JsonProperty("ValueofCurrentStockAmount")
	public long getValueofCurrentStockAmount() {
		return valueofCurrentStockAmount;
	}

	public void setValueofCurrentStockAmount(long valueofCurrentStockAmount) {
		this.valueofCurrentStockAmount = valueofCurrentStockAmount;
	}

}
