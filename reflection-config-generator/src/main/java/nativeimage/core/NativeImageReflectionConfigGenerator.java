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
import nativeimage.core.io.Out;
import nativeimage.core.io.ReflectionConfigWriter;

/**
 * Will generate native image reflection config to project source classes.
 */
public class NativeImageReflectionConfigGenerator implements Processor {

	private final Logger log = LoggerFactory.getLogger();
	private final ProcessingEnvironment processingEnv;
	private final Set<ReflectionConfig> classes;
	private String classPackage;

	public NativeImageReflectionConfigGenerator(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.classes = new LinkedHashSet<>();
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
				"m=processElementsForAnnotation, reflection=%s", reflection
		);
		this.addMatchingProjectSourceElements(roundEnv, element, reflection);
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
		final String fileName = "reflect.json";
		final String reflectFile = solvePath(classPackage, fileName);

		try (
				ReflectionConfigWriter appender =
						new ReflectionConfigWriter(Out.of(this.processingEnv, reflectFile));
		) {

			appender.writeAll(this.classes);

			final URI nativeImageFile = NativeImagePropertiesWriter.write(
					this.processingEnv, classPackage, fileName
			);
			log.info(
					"status=reflect-generation-done, objects=%d, path=%s",
					this.classes.size(), nativeImageFile
			);

			log.debug("objects=%s", this.classes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String getClassPackage() {
		return this.classPackage == null ? "graal-reflection-configuration" : this.classPackage;
	}
}
