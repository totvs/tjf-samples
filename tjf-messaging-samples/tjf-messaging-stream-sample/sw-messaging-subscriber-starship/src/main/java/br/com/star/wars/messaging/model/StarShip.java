package br.com.star.wars.messaging.model;

import org.springframework.messaging.support.GenericMessage;

import com.totvs.tjf.core.message.TOTVSMessage;

public class StarShip {

	private String name;
	
	public StarShip(GenericMessage<TOTVSMessage<StarShip>> name) {
		System.out.println("##### teste #####");
		this.name = name.getPayload().getContent().getName();
	}

	
	public StarShip(String name) {
		System.out.println("##### teste #####");
		this.name = name;
	}

	public StarShip() {
		System.out.println("##### teste #####");
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