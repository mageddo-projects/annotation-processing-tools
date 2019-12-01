package com.mageddo.aptools;

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
}
