package com.tjf.samples.treports.api;

import com.tjf.samples.treports.model.Report;

public class ApiCollectionReportReponse {

	private int total;
	private boolean hasNext = true;
	private Report[] items;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public Report[] getItems() {
		return items;
	}

	public void setItems(Report[] items) {
		this.items = items;
	}
}