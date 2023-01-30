package nativeimage.thirdparty;

import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

import com.mageddo.aptools.log.Logger;
import com.mageddo.aptools.log.LoggerFactory;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class ThirdPartyPackageScanner {

  private static final Logger log = LoggerFactory.getLogger();

  /**
   * Will scan all classes within a package.
   *
   * @param packageName something like com.mageddo
   */
  public static Set<Class<?>> findPackageClasses(String packageName) {
    final Set<String> classes = new Reflections(packageName)
        .getAll(Scanners.SubTypes);
    final Set<Class<?>> filteredClasses = new LinkedHashSet<>();
    for (String className : classes) {
      try {
        final Class<?> clazz = Class.forName(className);
        if (isValidClass(clazz)) {
          filteredClasses.add(clazz);
        } else {
          log.debug("status=notValidClass, class={}", className);
        }
      } catch (ClassNotFoundException e) {
        log.warn("status=classNotFound?, class={}", className, e);
      }
    }
    return filteredClasses;
  }

  static boolean isValidClass(Class<?> clazz) {
    return !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers());
  }
}
