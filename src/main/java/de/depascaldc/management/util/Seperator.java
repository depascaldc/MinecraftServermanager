package de.depascaldc.management.util;

public enum Seperator {
	OBJ("%"), SEP(":"), SEPLOC(";"), PART("&");
	private String character;
	Seperator(String character) {
		this.character = character;
	}
	public String getCharacter() {
		return character;
	}
}
