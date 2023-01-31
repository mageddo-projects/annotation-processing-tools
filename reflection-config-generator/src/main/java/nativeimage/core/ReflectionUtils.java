package nativeimage.core;

import nativeimage.Reflection;

import org.apache.commons.lang3.StringUtils;

import javax.lang.model.type.MirroredTypeException;

public class ReflectionUtils {
	public static boolean isVoid(nativeimage.Reflection reflection) {
		try {
			return reflection.scanClass() == Void.class;
		} catch (MirroredTypeException e) {
			return e.getTypeMirror().toString().startsWith(Void.class.getName());
		}
	}

	public static String getClassName(Reflection reflectionAnn){
		return StringUtils.firstNonBlank(getScanClass(reflectionAnn), reflectionAnn.scanClassName());
	}

	private static String getScanClass(Reflection reflectionAnn) {
		if (ReflectionUtils.isVoid(reflectionAnn)) {
			return null;
		}
		try {
			return reflectionAnn.scanClass().getName();
		} catch (MirroredTypeException e) {
			return e.getTypeMirror().toString();
		}
	}

}
