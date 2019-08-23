package br.com.starwars.familytree.enums;

public enum Gender {

	MALE("male"), FEMALE("female"), OTHER("other");

	private final String gender;

	private Gender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return gender;
	}

}
