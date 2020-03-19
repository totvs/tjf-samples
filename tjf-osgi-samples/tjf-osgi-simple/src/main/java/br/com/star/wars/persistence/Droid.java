package br.com.star.wars.persistence;

import org.springframework.data.annotation.Id;

public class Droid {

	@Id
	private int id;
	private String name;
	private String description;
	private int droidClass;

	public Droid() {

	}

	public Droid(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDroidClass() {
		return droidClass;
	}

	public void setDroidClass(int droidClass) {
		this.droidClass = droidClass;
	}

}
