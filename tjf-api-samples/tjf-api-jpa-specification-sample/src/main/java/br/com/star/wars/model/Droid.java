package br.com.star.wars.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Droid {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	private String function;

	private double height;

	public Droid() {

	}

	public Droid(int id, String name, String function, double height) {
		this.id = id;
		this.name = name;
		this.function = function;
		this.height = height;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

}
