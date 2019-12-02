package com.mageddo.aptools.textblock.visitor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.mageddo.aptools.textblock.LocalVariable;
import com.mageddo.aptools.textblock.converter.JavaParserAnnotationConverter;
import com.mageddo.aptools.textblock.converter.JavaParserExpressionStmtConverter;

public class ClassAnnotatedVariablesJavaParserScanner extends VoidVisitorAdapter<CompilationUnit> {

  private final Class<? extends Annotation> annotation;
  private final List<LocalVariable> variables;

  public ClassAnnotatedVariablesJavaParserScanner(Class<? extends Annotation> annotation) {
    this.annotation = annotation;
    this.variables = new ArrayList<>();
  }

  @Override
  public void visit(FieldDeclaration n, CompilationUnit arg){
    System.out.printf("field=%s%n", n);
    for (VariableDeclarator variable : n.getVariables()) {
      final LocalVariable localVariable = new LocalVariable()
          .setName(variable.getId().getName())
          .setAnnotations(JavaParserAnnotationConverter.toAnnotationNames(n.getAnnotations()))
          .setComment(getComment(n, variable));
      if(localVariable.getAnnotations().contains(annotation.getSimpleName())){
        this.variables.add(localVariable);
      }
    }

  }

  @Override
  public void visit(VariableDeclarator n, CompilationUnit arg){
    System.out.printf("VariableDeclarator=%s%n", n);
  }


  @Override
  public void visit(VariableDeclarationExpr declarationExpr, CompilationUnit arg){
    final List<LocalVariable> variables = JavaParserExpressionStmtConverter
        .toLocalVariables(declarationExpr);
    for (LocalVariable variable : variables) {
      if(variable.getAnnotations().contains(annotation.getSimpleName())){
        this.variables.add(variable);
      }
    }
  }

  @Override
  public void visit(ExpressionStmt n, CompilationUnit arg) {

//          System.out.println("visitado=" + n);
    super.visit(n, arg);
  }

  public List<LocalVariable> getVariables() {
    return variables;
  }

  private String getComment(FieldDeclaration field, VariableDeclarator variable) {
    if(variable.getComment() != null){
      return variable.getComment().getContent();
    } else if (field.getJavaDoc() != null){
      return field.getJavaDoc().getContent();
    } else if(field.getComment() != null){
      return field.getComment().getContent();
    }
    throw new IllegalStateException("@TextBlock fields must have comments: " + field);
  }
}
