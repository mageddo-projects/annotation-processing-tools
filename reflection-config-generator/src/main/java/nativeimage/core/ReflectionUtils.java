package nativeimage.core;

import javax.lang.model.type.MirroredTypeException;

public class ReflectionUtils {
	public static boolean isVoid(nativeimage.Reflection reflection) {
		try {
			return reflection.scanClass() == Void.class;
		} catch (MirroredTypeException e) {
			return e.getTypeMirror().toString().startsWith(Void.class.getName());
		}
	}
}
