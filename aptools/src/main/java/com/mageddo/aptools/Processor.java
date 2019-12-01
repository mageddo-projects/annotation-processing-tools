package com.mageddo.aptools;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

public interface Processor {
	/**
	 * Handle annotation process event and construct
	 */
	void process(Set<TypeElement> annotations, RoundEnvironment roundEnv);
}
