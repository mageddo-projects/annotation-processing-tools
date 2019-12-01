package com.acme;

public class Fruit {

	private String name;
	private String color;

	public Fruit() {
	}

	public Fruit(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public Fruit setName(String name) {
		this.name = name;
		return this;
	}

	public String getColor() {
		return color;
	}

	public Fruit setColor(String color) {
		this.color = color;
		return this;
	}

	public static class ShouldNotScan {

	}
}
