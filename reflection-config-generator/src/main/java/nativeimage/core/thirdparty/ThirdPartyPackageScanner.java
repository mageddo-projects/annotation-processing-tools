package nativeimage.core.thirdparty;

import java.util.LinkedHashSet;
import java.util.Set;

import com.mageddo.aptools.ClassUtils;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ThirdPartyPackageScanner {

//	private static final Logger log = LoggerFactory.getLogger();

	/**
	 * Will scan all classes within a package, nested classes also will be retrieved
	 *
	 * @param packageName something like com.mageddo
	 */
	public static Set<Class<?>> findPackageClasses(String packageName) {

//		Set<Class<?>> classes = new Reflections(
//				new ConfigurationBuilder()
//						.setScanners(new SubTypesScanner(false), new ResourcesScanner())
//						.addUrls(ClasspathHelper.forPackage(packageName, ClassLoader.getSystemClassLoader()))
//						.filterInputsBy(new FilterBuilder()
//								.includePackage(packageName))
//		)
//				.getSubTypesOf(Object.class);

		final Set<String> classes = new Reflections(packageName)
				.getAll(Scanners.SubTypes);

		final Set<Class<?>> filteredClasses = new LinkedHashSet<>();
		for (String clazzName : classes) {
			try {
				Class<?> clazz = ClassUtils.forName(clazzName);
				if (
						ClassUtils.doPackageOwnClass(packageName, clazz.getName())
								&& !clazz.isInterface()
								&& !clazz.isSynthetic()
				) {
					filteredClasses.add(clazz);
				}
			} catch (Exception e){
				System.out.printf("status=failedForClass, class=%s, msg=%s%n", clazzName, e.getMessage());;
			}
		}
//		final Set<String> filteredClasses = classes
//				.stream()
//				.filter(it -> ClassUtils.doPackageOwnClass(packageName, it))
//				.collect(Collectors.toSet());

//		log.debug(
//				"status=packageScanned, classes=%d, afterFilter=%d, package=%s",
//				classes.size(),filteredClasses.size(),packageName
//		);
		return filteredClasses;
	}

}
