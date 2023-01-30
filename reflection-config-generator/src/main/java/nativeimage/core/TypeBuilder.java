package nativeimage.core;

import java.util.Collections;
import java.util.Set;

import javax.lang.model.type.MirroredTypeException;

import com.mageddo.aptools.ClassUtils;

import org.apache.commons.lang3.StringUtils;

import nativeimage.Reflection;

public final class TypeBuilder {

	private TypeBuilder() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static Set<String> of(Reflection reflectionAnn, String clazzName) {
		final String expectedClassName = StringUtils.firstNonBlank(
				getScanClass(reflectionAnn), reflectionAnn.scanClassName()
		);
		if(StringUtils.isNotBlank(expectedClassName)){
			if(ClassUtils.doClassOwnPossibleSubClassOrIsTheSame(expectedClassName, clazzName)){
				return toSet(clazzName);
			}
		}
		if (StringUtils.isNotEmpty(reflectionAnn.scanPackage())) {
			if (ClassUtils.doPackageOwnClass(reflectionAnn.scanPackage(), clazzName)) {
				return toSet(clazzName);
			}
			return Collections.emptySet();
		}
		return toSet(clazzName);
	}

	private static String getScanClass(Reflection reflectionAnn) {
		if (reflectionAnn.scanClass() == Void.class) {
			return null;
		}
		try {
			return reflectionAnn.scanClass().getName();
		} catch (MirroredTypeException e) {
			return e.getTypeMirror().toString();
		}
	}

	private static Set<String> toSet(String type) {
		return Collections.singleton(type);
	}
}
