package com.acme;

import nativeimage.Reflection;

@Reflection(scanClass = ClassWithNestedClass.class)
public class ClassWithNestedClass {
	public static class NestedClass {

	}
}
