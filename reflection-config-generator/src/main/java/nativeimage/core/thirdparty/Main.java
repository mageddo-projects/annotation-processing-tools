package nativeimage.core.thirdparty;

import static nativeimage.core.thirdparty.ThirdPartyPackageScanner.findPackageClasses;

public class Main {
	public static void main(String[] args) {
		System.out.println("> Finding classes for package: " + args[0]);
		for (Class<?> clazz : findPackageClasses(args[0])) {
			System.out.println(clazz);
		}
	}
}
