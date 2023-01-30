package nativeimage.core.thirdparty;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

import com.mageddo.aptools.ClassUtils;
import com.mageddo.aptools.log.Logger;

import com.mageddo.aptools.log.LoggerFactory;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class ThirdPartyPackageScanner {

	private static final Logger log = LoggerFactory.getLogger();

	/**
	 * Will scan all classes within a package, nested classes also will be retrieved
	 *
	 * @param packageName something like com.mageddo
	 */
	public static Set<String> findPackageClasses(String packageName) {

//		final Set<Class<?>> classes = new Reflections(new ConfigurationBuilder()
//				.setScanners(Scanners.SubTypes.filterResultsBy(s -> true),
//						Scanners.Resources
//				)
//				.addUrls(ClasspathHelper.forJavaClassPath())
//				.filterInputsBy(new FilterBuilder()
//						.includePackage(packageName)))
//				.getSubTypesOf(Object.class);

		final Set<String> classes = new Reflections(packageName)
				.getAll(Scanners.SubTypes);


		final Set<String> filteredClasses = classes
				.stream()
				.filter(it -> ClassUtils.doPackageOwnClass(packageName, it))
				.collect(Collectors.toSet());

		log.warn(
				"status=packageScanned, classes=%d, afterFilter=%d, package=%s",
				classes.size(), filteredClasses.size(), packageName
		);
		return filteredClasses;
	}

	public static void main(String[] args) {
		final Set<String> classes = new Reflections("com.fasterxml.jackson.core.util")
				.getAll(Scanners.SubTypes);

		System.out.println(classes);


		final Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(Scanners.SubTypes.filterResultsBy(s -> true),
						Scanners.Resources
				)
				.addUrls(ClasspathHelper.forJavaClassPath())
				.filterInputsBy(new FilterBuilder()
						.includePackage("com.fasterxml.jackson.core.util")));

		System.out.println(reflections.getSubTypesOf(Object.class));
		;
	}

	static boolean isValidClass(Class<?> clazz) {
		return !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers());
	}
}
