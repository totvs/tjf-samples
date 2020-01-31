package com.tjf.sample.github.validation;

public class ErrorResponse {

	private final String timestamp;
	private final Integer status;
	private final String error;
	private final String message;
	private final String path;
	
	public ErrorResponse(String timestamp, Integer status, String error, String message, String path) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}
}
