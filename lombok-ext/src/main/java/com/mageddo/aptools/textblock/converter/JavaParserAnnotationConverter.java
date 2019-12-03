package com.mageddo.aptools.textblock.converter;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.expr.AnnotationExpr;

public class JavaParserAnnotationConverter {
  public static List<String> toAnnotationNames(List<AnnotationExpr> annotations) {
    final List<String> annotationNames = new ArrayList<>();
    for (AnnotationExpr annotation : annotations) {
      annotationNames.add(annotation.getName().getName());
    }
    return annotationNames;
  }
}
