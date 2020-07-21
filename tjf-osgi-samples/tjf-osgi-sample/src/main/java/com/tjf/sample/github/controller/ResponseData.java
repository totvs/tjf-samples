package com.tjf.sample.github.controller;

import java.util.List;

import com.tjf.sample.github.persistence.Droid;

public class ResponseData {

	private String queryType;
	private float time;
	private List<Droid> droid;

	public ResponseData() {
	}

	public ResponseData(String queryType, float time, List<Droid> droid) {
		this.queryType = queryType;
		this.time = time;
		this.droid = droid;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public List<Droid> getDroid() {
		return droid;
	}

	public void setDroid(List<Droid> droid) {
		this.droid = droid;
	}

}
