package com.acme;

public class Car {

	private String brand;
	private String model;

	public Car() {
	}

	public Car(String brand, String model) {
		this.brand = brand;
		this.model = model;
	}

	public String getBrand() {
		return brand;
	}

	public Car setBrand(String brand) {
		this.brand = brand;
		return this;
	}

	public String getModel() {
		return model;
	}

	public Car setModel(String model) {
		this.model = model;
		return this;
	}
}
