package nativeimage.core;

import java.util.Collections;
import java.util.Set;

import javax.lang.model.type.MirroredTypeException;

import com.mageddo.aptools.ClassUtils;

import nativeimage.Reflection;

public final class TypeBuilder {

	private TypeBuilder() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static Set<String> of(Reflection reflectionAnn, String clazzName) {
		if(!reflectionAnn.scanClassName().equals("")){
			return toSet(reflectionAnn.scanClassName());
		}
		final String scanClass = getScanClass(reflectionAnn);
		if(!scanClass.equals(Void.class.getName())){
			return toSet(scanClass);
		}
		if(!reflectionAnn.scanPackage().equals("")){
			if(ClassUtils.doPackageOwnClass(reflectionAnn.scanPackage(), clazzName)){
				return toSet(clazzName);
			}
			return Collections.emptySet();
		}
		return toSet(clazzName);
	}

	private static String getScanClass(Reflection reflectionAnn) {
		try {
			return reflectionAnn.scanClass().getName();
		} catch (MirroredTypeException e){
			return e.getTypeMirror().toString();
		}
	}

	private static Set<String> toSet(String type) {
		return Collections.singleton(type);
	}
}
