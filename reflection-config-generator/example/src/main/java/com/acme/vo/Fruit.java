package com.acme.vo;

public class Fruit {

	private String name;

	public String getName() {
		return name;
	}

	public Fruit setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String toString() {
		return "Fruit{" +
			"name='" + name + '\'' +
			'}';
	}

	public static class SubClass {

		private String subClassProp;

		public SubClass setSubClassProp(String subClassProp) {
			this.subClassProp = subClassProp;
			return this;
		}

		public String getSubClassProp() {
			return subClassProp;
		}
	}
}
