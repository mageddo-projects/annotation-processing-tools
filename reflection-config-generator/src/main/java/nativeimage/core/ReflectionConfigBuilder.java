package nativeimage.core;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import nativeimage.Reflection;
import nativeimage.core.domain.Method;
import nativeimage.core.domain.ReflectionConfig;

//@Experimental
public final class ReflectionConfigBuilder {

	private ReflectionConfigBuilder() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static Set<ReflectionConfig> of(final Reflection annotation, String clazzName){
		final Set<ReflectionConfig> reflectionConfigs = new LinkedHashSet<>();
		for (String type : TypeBuilder.of(annotation, clazzName)) {
			reflectionConfigs.add(
				toBuilder(annotation)
				.type(type)
				.build()
			);
		}
		return reflectionConfigs;
	}

	private static ReflectionConfig.ReflectionConfigBuilder toBuilder(Reflection reflectionAnn) {
		final ReflectionConfig.ReflectionConfigBuilder builder = ReflectionConfig
			.builder()
			.allPublicConstructors(reflectionAnn.publicConstructors())
			.allDeclaredConstructors(reflectionAnn.declaredConstructors())

			.allPublicFields(reflectionAnn.publicFields())
			.allDeclaredFields(reflectionAnn.declaredFields())

			.allPublicMethods(reflectionAnn.publicMethods())
			.allDeclaredMethods(reflectionAnn.declaredMethods())
		;
		if(reflectionAnn.constructors()){
			final List<Method> methods = new ArrayList<>();
			methods.add(Method.of("<init>"));
			builder.allDeclaredConstructors(true);
			builder.allPublicConstructors(true);
			builder.methods(methods);
		}
		return builder;
	}
}
