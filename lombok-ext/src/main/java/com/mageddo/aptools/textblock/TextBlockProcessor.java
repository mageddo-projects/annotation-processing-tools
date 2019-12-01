package com.mageddo.aptools.textblock;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import com.mageddo.aptools.Processor;
import com.mageddo.aptools.log.Logger;
import com.mageddo.aptools.log.LoggerFactory;

public class TextBlockProcessor implements Processor {
  private final Logger logger = LoggerFactory.getLogger();
  private final ProcessingEnvironment processingEnv;

  public TextBlockProcessor(ProcessingEnvironment processingEnv) {
    this.processingEnv = processingEnv;
  }

  @Override
  public void process(Set<TypeElement> annotations, RoundEnvironment roundEnv) {
    throw new UnsupportedOperationException();
  }
}
