package nativeimage.core;

import com.mageddo.aptools.ClassUtils;
import com.mageddo.aptools.IoUtils;
import com.mageddo.aptools.Processor;
import com.mageddo.aptools.elements.ElementFinder;
import com.mageddo.aptools.log.Logger;
import com.mageddo.aptools.log.LoggerFactory;
import nativeimage.Reflection;
import nativeimage.Reflections;
import nativeimage.core.domain.ReflectionConfig;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class NativeImageReflectionConfigGenerator implements Processor {

	private final Logger logger = LoggerFactory.getLogger();
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

	private void processElementsForAnnotation(RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getElementsAnnotatedWith(Reflection.class)) {
			processElementsForAnnotation(roundEnv, element, element.getAnnotation(Reflection.class));
		}
	}

	private void processElementsForAnnotation(RoundEnvironment roundEnv, Element element, Reflection reflection) {
		if(reflection.scanPackage().isEmpty()){
			this.addElement(element, reflection);
		} else {
			for (final Element nestedElement : roundEnv.getRootElements()) {
				this.addElement(nestedElement, reflection);
				for (final Element innerClass : ElementFinder.find(nestedElement, ElementKind.CLASS)) {
					this.addElement(innerClass, reflection);
					logger.debug("innerClass=%s", innerClass);
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
		this.classPackage = this.classPackage == null ? ClassUtils.getClassPackage(element.toString()) : this.classPackage;
		for (ReflectionConfig config : ReflectionConfigBuilder.of(element, annotation)) {
			this.classes.remove(config);
			this.classes.add(config);
		}
	}

	private void writeObjects() {
		ReflectionConfigAppenderAnnotationProcessing appender = null;
		try {
			appender = new ReflectionConfigAppenderAnnotationProcessing(this.processingEnv, getClassPackage());
			for (ReflectionConfig config : this.classes) {
				appender.append(config);
			}
			logger.info("native-image-reflection-configuration, written-objects=%d", this.classes.size());
			logger.debug("objects=%s", this.classes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IoUtils.safeClose(appender);
		}
	}

	private String getClassPackage() {
		return this.classPackage == null ? "graal-reflection-configuration" : this.classPackage;
	}
}
