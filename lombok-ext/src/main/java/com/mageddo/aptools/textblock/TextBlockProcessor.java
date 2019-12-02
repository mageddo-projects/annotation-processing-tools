package com.mageddo.aptools.textblock;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.mageddo.aptools.Processor;
import com.mageddo.aptools.log.Logger;
import com.mageddo.aptools.log.LoggerFactory;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;

import lombok.TextBlock;

public class TextBlockProcessor implements Processor {

  private static final Class<TextBlock> TEXT_BLOCK_CLASS = TextBlock.class;

  private final Logger logger = LoggerFactory.getLogger();
  private final Trees trees;
  private final TreeMaker treeMaker;

  public TextBlockProcessor(ProcessingEnvironment processingEnv) {
    this.trees = Trees.instance(processingEnv);
    this.treeMaker = TreeMaker.instance(((JavacProcessingEnvironment) processingEnv).getContext());
  }

  @Override
  public void process(Set<TypeElement> annotations, RoundEnvironment roundEnv) {
    try {
      logger.warn("processingover=%s, tmp: %s", roundEnv.processingOver(), roundEnv
          .getRootElements());
      for (final Element element : roundEnv.getRootElements()) {

        final List<VariableTree> classVars = getClassVars(element);
        final List<LocalVariable> sourceFileVars = getSourceFileVars((Symbol.ClassSymbol) element);

        TextBlockVariableFiller.fill(this.treeMaker, classVars, sourceFileVars);

      }
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  private List<LocalVariable> getSourceFileVars(Symbol.ClassSymbol element) {
    try (Reader reader = element.sourcefile.openReader(true)) {
      final ClassAnnotatedVariablesJavaParserScanner javaParserScanner =
          new ClassAnnotatedVariablesJavaParserScanner(TEXT_BLOCK_CLASS);
      JavaParser.parse(reader, true).accept(javaParserScanner, null);
      return javaParserScanner.getVariables();
    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }
  }

  private List<VariableTree> getClassVars(Element element) {
    final ClassAnnotatedVariableTreePathScanner apScanner =
        new ClassAnnotatedVariableTreePathScanner(TEXT_BLOCK_CLASS);
    apScanner.scan(this.trees.getPath(element), element);
    return apScanner.getVariables();
  }
}
