package com.mageddo.aptools.textblock;

import java.util.Collections;
import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;

public class JavaParserExpressionStmtConverter {
  public static List<LocalVariable> toLocalVariables(ExpressionStmt stm) {
    for (Node node : stm.getChildrenNodes()) {
      if (node instanceof VariableDeclarationExpr) {
        final VariableDeclarationExpr varDeclar = (VariableDeclarationExpr) node;
        for (final VariableDeclarator var : varDeclar.getVars()) {
            return Collections.singletonList(new LocalVariable()
                .setName(var.getId().getName())
                .setComment(stm.getComment().getContent())
                .setAnnotations(JavaParserAnnotationConverter.toAnnotationNames(varDeclar.getAnnotations()))
            );
          }
        }
    }
    return Collections.emptyList();
  }
}
