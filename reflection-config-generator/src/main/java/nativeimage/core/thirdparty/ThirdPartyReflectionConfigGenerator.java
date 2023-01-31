package nativeimage.core.thirdparty;

import java.util.Set;

import com.mageddo.aptools.ClassUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nativeimage.Reflection;
import static nativeimage.core.ReflectionUtils.getClassName;
import static nativeimage.core.thirdparty.ThirdPartyPackageScanner.findPackageClasses;

public class ThirdPartyReflectionConfigGenerator {

	private final Logger log = LoggerFactory.getLogger(getClass());

	void addClass(Class<?> clazz, Reflection reflection) {
		log.debug("m=addClass, clazz=%s", clazz, clazz);
		// todo check if the name is correct
//		for (final ReflectionConfig config : ReflectionConfigBuilder.of(reflection, clazz)) {
//		}
	}


	void addClassAndNested(Reflection reflection, Class<?> clazz) {
		this.addClass(clazz, reflection);
		for (final Class<?> innerClass : ClassUtils.findNestClasses(clazz)) {
			this.addClass(innerClass, reflection);
			log.debug("m=addMatchingProjectLibsClasses, innerClass=%s", innerClass);
		}
	}

	void addMatchingProjectLibsClasses(Reflection reflection) {
		if (reflection.scanPackage().isEmpty()) {
			this.addClassAndNested(reflection, ClassUtils.forName(getClassName(reflection)));
		} else {
			final Set<Class<?>> classes = findPackageClasses(reflection.scanPackage());
			for (final Class<?> clazz : classes) {
				this.addClass(clazz, reflection);
			}
		}
	}

}
