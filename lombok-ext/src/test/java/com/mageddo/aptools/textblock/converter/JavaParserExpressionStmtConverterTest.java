package com.mageddo.aptools.textblock.converter;

import java.util.List;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.PrimitiveType;
import com.mageddo.aptools.textblock.LocalVariable;

import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class JavaParserExpressionStmtConverterTest {

  @Test
  void mustSetNullCommentWhenItIsNotAvailable() {
    // arrange

    final VariableDeclarator varDeclarator = new VariableDeclarator();
    varDeclarator.setId(new VariableDeclaratorId("str"));
    final VariableDeclarationExpr childVarDeclarationExpr = new VariableDeclarationExpr(
        new PrimitiveType(), singletonList(varDeclarator)
    );

    final Statement stm = mock(Statement.class);
    doReturn(singletonList(childVarDeclarationExpr)).when(stm).getChildrenNodes();

    // act
    final List<LocalVariable> variables = JavaParserExpressionStmtConverter.toLocalVariables(stm);

    // assert
    assertEquals(1, variables.size());
    final LocalVariable firstVar = variables.get(0);
    assertEquals("str", firstVar.getName());
    assertNull(firstVar.getComment());
    assertEquals(0, firstVar.getAnnotations().size());
  }
}
