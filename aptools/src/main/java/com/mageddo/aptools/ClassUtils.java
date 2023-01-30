package com.mageddo.aptools;

import java.util.List;

public class ClassUtils {
	private ClassUtils() {
	}

	public static String getClassPackage(String className) {
		final int lastIndexOf = className.lastIndexOf('.');
		if(lastIndexOf < 0){
			return className;
		}
		return className.substring(0, lastIndexOf);
	}

	public static boolean doPackageOwnClass(String packageName, String className) {
		return getClassPackage(className).contains(packageName);
	}

	public static List<Class<?>> findNestClasses(Class<?> clazz){
		throw new UnsupportedOperationException();
	}

	public static Class<?> forName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean doClassOwnPossibleSubClassOrIsTheSame(String expected, String current) {
		return current.startsWith(expected); // todo talvez tenha que ser melhor validado
	}
}
