package nativeimage.core;

import java.util.LinkedHashSet;
import java.util.Set;

import nativeimage.Reflection;
import nativeimage.core.domain.ReflectionConfig;
import static nativeimage.core.domain.ReflectionConfig.withConstructors;

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
			withConstructors(builder);
		}
		return builder;
	}
}
