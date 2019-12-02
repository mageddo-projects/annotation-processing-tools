package com.mageddo.aptools.textblock;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.mageddo.aptools.Processor;
import com.mageddo.aptools.elements.ElementFinder;
import com.mageddo.aptools.log.Logger;
import com.mageddo.aptools.log.LoggerFactory;
import com.mageddo.aptools.util.Predicate;
import com.sun.tools.javac.code.Symbol;

import lombok.TextBlock;

public class TextBlockProcessor implements Processor {
  private final Logger logger = LoggerFactory.getLogger();
  private final ProcessingEnvironment processingEnv;
  private Element lastClass;

  public TextBlockProcessor(ProcessingEnvironment processingEnv) {
    this.processingEnv = processingEnv;
  }

  @Override
  public void process(Set<TypeElement> annotations, RoundEnvironment roundEnv) {
    try {
    logger.warn("processingover=%s, tmp: %s", roundEnv.processingOver(), roundEnv.getRootElements());
    for (final Element element : roundEnv.getRootElements()) {
      this.lastClass = element;
      final List<Element> textBlockVar = ElementFinder.find(element,
          textBlockAnnotatedElementPredicate()
      );
      List<Element> childs = ElementFinder.find(element, new Predicate<Element>() {
        @Override
        public boolean test(Element element) {
          return true;
        }
      });
      javaParserMethodToExtractVars(roundEnv, (Symbol.ClassSymbol) element);
      logger.warn("class=%s, textBlockVar=%s, children=%s", element, textBlockVar, childs);

    }
    if(roundEnv.processingOver()){
      logger.warn("writing to class " + this.lastClass);
      Symbol.ClassSymbol lastClassUnit = (Symbol.ClassSymbol) this.lastClass;
      CompilationUnit cu = JavaParser.parse(lastClassUnit.sourcefile.openReader(true), true);
//      System.out.println("classfile: " + lastClassUnit.sourcefile);
      System.out.println(cu.toString());
      Writer writer = lastClassUnit.sourcefile.openWriter();
      writer.write(cu.toString());
      writer.close();
    }

    }catch (Exception e){
//      logger.error(ExceptionUtils.getStackTrace(e));
      logger.error(e.getMessage());
    }
  }

  private void javaParserMethodToExtractVars(RoundEnvironment roundEnv, Symbol.ClassSymbol element) throws ParseException, IOException {
    final CompilationUnit cu = JavaParser.parse(element.sourcefile.openReader(true), true);
    recursivePrint(cu);
  }

  private void recursivePrint(Node cu) {
    for (Node childrenNode : cu.getChildrenNodes()) {
//      System.out.printf("child=%s%n", childrenNode.toString());
      recursivePrint(childrenNode);
    }
  }

  public Predicate<Element> textBlockAnnotatedElementPredicate(){
    return new Predicate<Element>(){
      @Override
      public boolean test(Element o) {
        return o.getAnnotation(TextBlock.class) != null;
      }
    };
  }
}
