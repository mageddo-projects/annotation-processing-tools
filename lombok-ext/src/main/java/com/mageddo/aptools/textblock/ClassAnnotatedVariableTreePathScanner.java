package com.mageddo.aptools.textblock;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePathScanner;

public class ClassAnnotatedVariableTreePathScanner extends TreePathScanner<Object, Element> {

  private final Class<? extends Annotation> annotation;
  private final List<VariableTree> annotatedVars;

  public ClassAnnotatedVariableTreePathScanner(Class<? extends Annotation> annotation) {
    this.annotation = annotation;
    this.annotatedVars = new ArrayList<>();
  }

  @Override
  public Object visitVariable(VariableTree variableTree, Element element) {
    System.out.println("var: " + variableTree);
    for (AnnotationTree annotation : variableTree.getModifiers().getAnnotations()) {
      System.out.println("annotation: " + annotation.getAnnotationType().toString());
      if(annotation.getAnnotationType().toString().equals(this.annotation.getSimpleName())){
        this.annotatedVars.add(variableTree);
      }
    }
    return super.visitVariable(variableTree, element);
  }

  public List<VariableTree> getVariables() {
    return annotatedVars;
  }
}
