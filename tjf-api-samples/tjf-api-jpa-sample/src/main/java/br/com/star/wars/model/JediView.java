package br.com.star.wars.model;

import java.util.List;

public interface JediView {

	public String getName();

	public String getGender();

	public List<Movie> getMovies();
	
	public interface MovieView {
		int getId();
	}
}