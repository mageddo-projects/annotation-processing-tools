package com.mageddo.aptools.textblock;

import java.io.IOException;

import javax.lang.model.element.Element;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePathScanner;
import com.sun.tools.javac.code.Symbol;

public class VariableTreePathScanner extends TreePathScanner<Object, Element> {

  private String method;

  @Override
  public Object visitMethod(MethodTree methodTree, Element element) {
    this.method = methodTree.getName().toString();
    return super.visitMethod(methodTree, element);
  }

  @Override
  public Object visitVariable(VariableTree variableTree, Element element) {
    try {
      System.out.println("var: " + variableTree);
//      System.out.println(variableTree.);
      System.out.println();
      Symbol.ClassSymbol aClass = (Symbol.ClassSymbol) element;
      final CompilationUnit parsedSourceFile = JavaParser.parse(
          aClass.sourcefile.openReader(true), true
      );
      parsedSourceFile.accept(new VoidVisitorAdapter<CompilationUnit>() {
        @Override
        public void visit(ExpressionStmt n, CompilationUnit arg) {
          System.out.println("visitado=" + n);
          super.visit(n, arg);
        }
      }, parsedSourceFile);
      return super.visitVariable(variableTree, element);
    } catch (ParseException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
