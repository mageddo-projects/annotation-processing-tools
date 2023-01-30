package nativeimage.core;

public class NativeImages {

	public static String solvePath(String classPackage, final String fileName) {
		return String.format("META-INF/native-image/%s/%s", classPackage, fileName);
	}
}
