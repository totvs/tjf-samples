package com.tjf.samples.treports.api;

public class ApiExecuteInfoResponse {

	private String uIdRequest;
	private String scheduleDate;

	public String getuIdRequest() {
		return uIdRequest;
	}

	public void setuIdRequest(String uIdRequest) {
		this.uIdRequest = uIdRequest;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
}