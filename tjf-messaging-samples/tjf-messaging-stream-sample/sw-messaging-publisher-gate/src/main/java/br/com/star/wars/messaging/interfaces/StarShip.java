package br.com.star.wars.messaging.interfaces;

public class StarShip {
	
	private String name;
	
	public StarShip(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return this.name;
	}
}
