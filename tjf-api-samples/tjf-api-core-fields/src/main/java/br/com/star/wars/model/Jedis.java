package br.com.star.wars.model;

public class Jedis {

	private String name;

	private String gender;

	private String species;

	private double height;

	public Jedis(String name, String gender, String species, double height) {
		this.name = name;
		this.gender = gender;
		this.species = species;
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

}
