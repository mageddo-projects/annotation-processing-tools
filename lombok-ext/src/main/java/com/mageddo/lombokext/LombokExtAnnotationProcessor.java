package com.mageddo.lombokext;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import com.mageddo.aptools.Processor;
import com.mageddo.aptools.log.Logger;
import com.mageddo.aptools.log.LoggerFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class LombokExtAnnotationProcessor extends AbstractProcessor {

	private Logger logger;
	private List<Processor> processors;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.logger = LoggerFactory.bindLogger(this.processingEnv.getMessager());
		this.processors = new ArrayList<>();
//		this.processors.add(new TextBlockProcessor(processingEnv));

	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		try {
			for (Processor processor : this.processors) {
				processor.process(new LinkedHashSet<>(annotations), roundEnv);
			}
		} catch (Exception e){
			this.logger.error("fatal: %s\n ", e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

}
