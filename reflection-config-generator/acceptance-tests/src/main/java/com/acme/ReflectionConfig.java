package com.acme;

import nativeimage.Reflection;
import nativeimage.Reflections;

@Reflections({
	@Reflection(
		scanClass = Fruit.class,
		declaredFields = true, publicFields = true,
		declaredMethods = true, publicMethods = true,
		declaredConstructors = true, publicConstructors = true
	),
	@Reflection(
		scanClassName = "com.acme.Car",
		declaredConstructors = true, declaredFields = true, declaredMethods = true
	),
	@Reflection(
		scanPackage = "com.acme.subpackage",
		declaredFields = true
	)
})
public class ReflectionConfig {
}
