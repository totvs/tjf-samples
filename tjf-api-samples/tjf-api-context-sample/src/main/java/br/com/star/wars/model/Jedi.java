package br.com.star.wars.model;

import java.util.List;

public class Jedi {
	private String name;
	private String gender;
	private List<Integer> movies;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<Integer> getMovies() {
		return this.movies;
	}

	public void setMovies(List<Integer> movies) {
		this.movies = movies;
	}

}