package com.acme;

import nativeimage.Reflection;
import nativeimage.Reflections;

@Reflections({
		@Reflection(
				scanClass = Fruit.class,
				declaredFields = true,
				publicFields = true,
				declaredMethods = true,
				publicMethods = true,
				declaredConstructors = true,
				publicConstructors = true
		),
		@Reflection(
				scanClassName = "com.acme.Car",
				declaredConstructors = true,
				declaredFields = true,
				declaredMethods = true
		),
		@Reflection(
				scanPackage = "com.acme.subpackage",
				declaredFields = true
		),
		@Reflection(
//				scanPackage = "org.apache.commons.io.monitor",
				scanPackage = "com.fasterxml.jackson.core.util",
				scanLibs = true
		)
})
public class ReflectionConfig {
}
