package nativeimage.core;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import com.mageddo.aptools.ClassUtils;
import com.mageddo.aptools.Processor;
import com.mageddo.aptools.elements.ElementFinder;
import com.mageddo.aptools.elements.ElementUtils;
import com.mageddo.aptools.log.Logger;
import com.mageddo.aptools.log.LoggerFactory;

import nativeimage.Reflection;
import nativeimage.Reflections;
import nativeimage.core.domain.ReflectionConfig;
import static nativeimage.core.NativeImages.solvePath;
import static nativeimage.thirdparty.ThirdPartyPackageScanner.findPackageClasses;

/**
 * Will generate native image reflection config to project source classes.
 */
public class NativeImageReflectionConfigGenerator implements Processor {

	private final Logger logger = LoggerFactory.getLogger();
	private final ProcessingEnvironment processingEnv;
	private final Set<ReflectionConfig> classes;
	private final Set<ReflectionConfig> thirdPartyClasses;
	private String classPackage;

	public NativeImageReflectionConfigGenerator(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.classes = new LinkedHashSet<>();
		this.thirdPartyClasses = new LinkedHashSet<>();
	}

	@Override
	public void process(Set<TypeElement> annotations, RoundEnvironment roundEnv) {
		final boolean processingOver = roundEnv.processingOver();
		processElementsForRepeatableAnnotation(roundEnv);
		processElementsForAnnotation(roundEnv);
		if (processingOver) {
			writeObjects();
		}
	}

	private void processElementsForRepeatableAnnotation(RoundEnvironment roundEnv) {
		final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Reflections.class);
		for (Element element : elements) {
			processElementsForRepeatableAnnotation(roundEnv, element);
		}
	}

	private void processElementsForRepeatableAnnotation(RoundEnvironment roundEnv, Element element) {
		final Reflections reflections = element.getAnnotation(Reflections.class);
		for (final Reflection reflection : reflections.value()) {
			processElementsForAnnotation(roundEnv, element, reflection);
		}
	}

	void processElementsForAnnotation(RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getElementsAnnotatedWith(Reflection.class)) {
			processElementsForAnnotation(roundEnv, element, element.getAnnotation(Reflection.class));
		}
	}

	void processElementsForAnnotation(
			RoundEnvironment roundEnv, Element element, Reflection reflection
	) {
		if (reflection.scanLibs()) {
			this.addMatchingProjectLibsClasses(reflection);
		} else {
			this.addMatchingProjectSourceElements(roundEnv, element, reflection);
		}
	}

	void addMatchingProjectLibsClasses(Reflection reflection) {
		if (reflection.scanPackage().isEmpty()) {
			this.addClassAndNested(reflection, chooseClass(reflection));
		} else {
			final Set<Class<?>> classes = findPackageClasses(reflection.scanPackage());
			for (final Class<?> clazz : classes) {
				this.addClassAndNested(reflection, clazz);
			}
		}
	}

	static Class<?> chooseClass(Reflection reflection) {
		return reflection.scanClass() != Void.class
				? reflection.scanClass()
				: ClassUtils.forName(reflection.scanClassName());
	}

	void addClassAndNested(Reflection reflection, Class<?> clazz) {
		this.addClass(clazz, reflection);
		for (final Class<?> innerClass : ClassUtils.findNestClasses(clazz)) {
			this.addClass(innerClass, reflection);
			logger.debug("m=addMatchingProjectLibsClasses, innerClass=%s", innerClass);
		}
	}

	void addClass(Class<?> clazz, Reflection reflection) {
		logger.debug("m=addClass, clazz=%s", clazz, clazz.getName());
		// todo check if the name is correct
		for (ReflectionConfig config : ReflectionConfigBuilder.of(reflection, clazz.getName())) { //
			this.thirdPartyClasses.remove(config);
			this.thirdPartyClasses.add(config);
		}
	}

	void addMatchingProjectSourceElements(RoundEnvironment roundEnv, Element element,
			Reflection reflection) {
		if (reflection.scanPackage().isEmpty()) {
			this.addElement(element, reflection);
			// todo must add nested classes too
		} else {
			for (final Element nestedElement : roundEnv.getRootElements()) {
				this.addElement(nestedElement, reflection);
				for (final Element innerClass : ElementFinder.find(nestedElement, ElementKind.CLASS)) {
					this.addElement(innerClass, reflection);
					logger.debug("m=addMatchingProjectSourceElements, innerClass=%s", innerClass);
				}
			}
		}
	}

	private void addElement(Element element, Reflection annotation) {
//		final Symbol.ClassSymbol symbol = (Symbol.ClassSymbol) element;
//		((Symbol.ClassSymbol) element).sourcefile.de
		logger.debug(
				"m=addElement, asType=%s, kind=%s, simpleName=%s, enclosing=%s, clazz=%s",
				element.asType(), element.getKind(), element.getSimpleName(),
				element.getEnclosingElement(), element.getClass()
		);
		this.classPackage = this.classPackage == null ?
				ClassUtils.getClassPackage(element.toString()) : this.classPackage;
		for (ReflectionConfig config : ReflectionConfigBuilder.of(annotation,
				ElementUtils.toClassName(element))) {
			this.classes.remove(config);
			this.classes.add(config);
		}
	}

	private void writeObjects() {
		final String classPackage = this.getClassPackage();
		final String reflectFile = solvePath(classPackage, "reflect.json");
		final String reflectFileThirdParty = solvePath(classPackage, "reflect-third-party.json");
		try (
				ReflectionConfigWriter appender =
						new ReflectionConfigWriter(this.processingEnv, reflectFile);

				ReflectionConfigWriter appenderThirdParty =
						new ReflectionConfigWriter(this.processingEnv, reflectFileThirdParty)
		) {

			appender.writeAll(this.classes);
			appenderThirdParty.writeAll(this.thirdPartyClasses);

			final URI nativeImageFile = NativeImagePropertiesWriter.write(
					this.processingEnv, classPackage, reflectFile, reflectFileThirdParty
			);
			logger.info(
					"status=reflect-generation-done, objects=%d, 3rdObjects=%s, path=%s",
					this.classes.size(), this.thirdPartyClasses.size(), nativeImageFile
			);

			logger.debug("objects=%s", this.classes);
			logger.debug("3rdObjects=%s", this.classes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String getClassPackage() {
		return this.classPackage == null ? "graal-reflection-configuration" : this.classPackage;
	}
}
