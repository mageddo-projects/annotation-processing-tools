package com.mageddo.aptools;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClassUtilsTest {

	@Test
	void mustFindClassPackage(){
		// arrange
		final String className = Map.class.getName();

		// act
		final String classPackage = ClassUtils.getClassPackage(className);

		// assert
		Assertions.assertEquals("java.util", classPackage);
	}

	@Test
	void mustMatchInnerClass(){
		// arrange

		// act
		final boolean own = ClassUtils.doPackageOwnClass("com.acme", "com.acme.UpperClass$InnerClass");

		// assert
		Assertions.assertTrue(own);
	}
}
