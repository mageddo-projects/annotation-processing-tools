package nativeimage.core.thirdparty;

import java.util.LinkedHashSet;
import java.util.Set;

import com.mageddo.aptools.ClassUtils;
import com.mageddo.aptools.log.Logger;
import com.mageddo.aptools.log.LoggerFactory;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ThirdPartyPackageScanner {

	private static final Logger log = LoggerFactory.getLogger();

	/**
	 * Will scan all classes within a package, nested classes also will be retrieved
	 *
	 * @param packageName something like com.mageddo
	 */
	public static Set<String> findPackageClasses(String packageName) {

		final Set<String> classes = new Reflections(packageName)
				.getAll(Scanners.SubTypes);

		final Set<String> filteredClasses = new LinkedHashSet<>();
		for (String clazz : classes) {
			if (ClassUtils.doPackageOwnClass(packageName, clazz)) {
				filteredClasses.add(clazz);
			}
		}
//		final Set<String> filteredClasses = classes
//				.stream()
//				.filter(it -> ClassUtils.doPackageOwnClass(packageName, it))
//				.collect(Collectors.toSet());

		log.warn(
				"status=packageScanned, classes=%d, afterFilter=%d, package=%s",
				classes.size(), filteredClasses.size(), packageName
		);
		return filteredClasses;
	}

}
