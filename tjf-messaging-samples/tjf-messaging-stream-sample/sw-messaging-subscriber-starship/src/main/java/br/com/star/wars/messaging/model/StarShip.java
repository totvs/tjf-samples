package br.com.star.wars.messaging.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StarShip {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	public UUID getId() {
		return id;
	}

	private String name;

	public StarShip(String name) {
		this.name = name;
	}

	public StarShip() {
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