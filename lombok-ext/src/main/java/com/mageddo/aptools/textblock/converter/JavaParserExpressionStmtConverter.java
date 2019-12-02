package com.mageddo.aptools.textblock.converter;

import java.util.Collections;
import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.mageddo.aptools.textblock.LocalVariable;

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

  public static List<LocalVariable> toLocalVariables(VariableDeclarationExpr declarationExpr) {
    for (VariableDeclarator varDeclarator : declarationExpr.getVars()) {
      return Collections.singletonList(new LocalVariable()
          .setName(varDeclarator.getId().getName())
          .setComment(getComment(declarationExpr))
          .setAnnotations(JavaParserAnnotationConverter.toAnnotationNames(declarationExpr.getAnnotations()))
      );
    }
    return Collections.emptyList();
  }

  private static String getComment(Node node) {
    if(node.getComment() != null){
      return node.getComment().getContent();
    }
    if(node.getParentNode().getComment() != null){
      return node.getParentNode().getComment().getContent();
    }
    return null;
  }
}
