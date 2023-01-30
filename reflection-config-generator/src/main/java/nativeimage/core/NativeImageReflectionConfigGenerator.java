package nativeimage.core;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.mageddo.aptools.ClassUtils;
import com.mageddo.aptools.Processor;
import com.mageddo.aptools.elements.ElementFinder;
import com.mageddo.aptools.elements.ElementUtils;
import com.mageddo.aptools.log.Logger;
import com.mageddo.aptools.log.LoggerFactory;

import org.apache.commons.lang3.StringUtils;

import static com.mageddo.aptools.elements.ElementUtils.toClassName;
import nativeimage.Reflection;
import nativeimage.Reflections;
import static nativeimage.core.NativeImages.solvePath;
import nativeimage.core.domain.ReflectionConfig;
import nativeimage.core.io.NativeImagePropertiesWriter;
import nativeimage.core.io.ReflectionConfigWriter;
import static nativeimage.core.thirdparty.ThirdPartyPackageScanner.findPackageClasses;

/**
 * Will generate native image reflection config to project source classes.
 */
public class NativeImageReflectionConfigGenerator implements Processor {

	private final Logger log = LoggerFactory.getLogger();
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
		log.debug(
				"m=processElementsForAnnotation, scanLibs=%b, reflection=%s",
				reflection.scanLibs(), reflection
		);
		if (reflection.scanLibs()) {
			this.addMatchingProjectLibsClasses(reflection);
		} else {
			this.addMatchingProjectSourceElements(roundEnv, element, reflection);
		}
	}

	void addMatchingProjectLibsClasses(Reflection reflection) {
		if (reflection.scanPackage().isEmpty()) {
			this.addClassAndNested(reflection, ReflectionUtils.getClassName(reflection));
		} else {
			final Set<String> classes = findPackageClasses(reflection.scanPackage());
			for (final String clazz : classes) {
				this.addClass(clazz, reflection);
			}
		}
	}

	void addClassAndNested(Reflection reflection, String clazz) {
		this.addClass(clazz, reflection);
		for (final String innerClass : ClassUtils.findNestClasses(clazz)) {
			this.addClass(innerClass, reflection);
			log.debug("m=addMatchingProjectLibsClasses, innerClass=%s", innerClass);
		}
	}

	void addClass(String clazz, Reflection reflection) {
		log.debug("m=addClass, clazz=%s", clazz, clazz);
		// todo check if the name is correct
		for (final ReflectionConfig config : ReflectionConfigBuilder.of(reflection, clazz)) {
			this.thirdPartyClasses.remove(config);
			this.thirdPartyClasses.add(config);
		}
	}

	void addMatchingProjectSourceElements(
			RoundEnvironment roundEnv, Element element, Reflection reflection
	) {
		if (reflection.scanPackage().isEmpty()) {
			final Element found = this.chooseElement(element, reflection, roundEnv);
			if (found != null) {
				this.addToElementAndNested(reflection, found);
			}
		} else {
			for (final Element nestedElement : roundEnv.getRootElements()) {
				this.addToElementAndNested(reflection, nestedElement);
			}
		}
	}

	private Element chooseElement(
			Element element, Reflection reflection, RoundEnvironment roundEnv
	) {
		final String className = ReflectionUtils.getClassName(reflection);
		if (StringUtils.isNotBlank(className)) {
			return this.findElementAndNested(className, roundEnv);
		}
		return element;
	}

	private Element findElementAndNested(String className, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getRootElements()) {
			if (ElementUtils.isEquals(element, className)) {
				return element;
			}
			for (Element nestedClass : ElementFinder.findNestedClasses(element)) {
				if (ElementUtils.isEquals(element, className)) {
					return nestedClass;
				}
			}
		}
		log.info("status=classNotFound, class={}", className);
		return null;
	}

	private void addToElementAndNested(Reflection reflection, Element element) {
		this.addElement(element, reflection);
		for (final Element innerClass : ElementFinder.findNestedClasses(element)) {
			this.addElement(innerClass, reflection);
			log.debug("m=addMatchingProjectSourceElements, innerClass=%s", innerClass);
		}
	}

	private void addElement(Element element, Reflection annotation) {
		log.debug(
				"m=addElement, asType=%s, kind=%s, simpleName=%s, enclosing=%s, clazz=%s",
				element.asType(), element.getKind(), element.getSimpleName(),
				element.getEnclosingElement(), element.getClass()
		);
		this.classPackage = this.classPackage == null ?
				ClassUtils.getClassPackage(element.toString()) : this.classPackage;
		for (ReflectionConfig config : ReflectionConfigBuilder.of(annotation, toClassName(element))) {
			this.classes.remove(config);
			this.classes.add(config);
		}
	}

	private void writeObjects() {

		final String classPackage = this.getClassPackage();
		final String reflectFile = solvePath(classPackage, "reflect.json");
		final String reflectFileThirdParty = solvePath(classPackage, "reflect-third-party.json");

		this.subtractSourceFromThirdPartyClass();

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
			log.info(
					"status=reflect-generation-done, objects=%d, 3rdObjects=%s, path=%s",
					this.classes.size(), this.thirdPartyClasses.size(), nativeImageFile
			);

			log.debug("objects=%s", this.classes);
			log.debug("3rdObjects=%s", this.classes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	boolean subtractSourceFromThirdPartyClass() {
		return this.classes.removeAll(this.thirdPartyClasses);
	}

	private String getClassPackage() {
		return this.classPackage == null ? "graal-reflection-configuration" : this.classPackage;
	}
}
