package com.mageddo.aptools.textblock.visitor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.mageddo.aptools.textblock.LocalVariable;
import com.mageddo.aptools.textblock.converter.JavaParserExpressionStmtConverter;

public class ClassAnnotatedVariablesJavaParserScanner extends VoidVisitorAdapter<CompilationUnit> {

  private final Class<? extends Annotation> annotation;
  private final List<LocalVariable> variables;

  public ClassAnnotatedVariablesJavaParserScanner(Class<? extends Annotation> annotation) {
    this.annotation = annotation;
    this.variables = new ArrayList<>();
  }

  @Override
  public void visit(ExpressionStmt n, CompilationUnit arg) {
    final List<LocalVariable> variables = JavaParserExpressionStmtConverter
        .toLocalVariables(n);
    for (LocalVariable variable : variables) {
      if(variable.getAnnotations().contains(annotation.getSimpleName())){
        this.variables.add(variable);
      }
    }
//          System.out.println("visitado=" + n);
    super.visit(n, arg);
  }

  public List<LocalVariable> getVariables() {
    return variables;
  }
}
