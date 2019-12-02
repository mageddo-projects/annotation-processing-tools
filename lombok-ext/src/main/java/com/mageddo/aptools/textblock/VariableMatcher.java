package com.mageddo.aptools.textblock;

import java.util.List;

import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;

import org.apache.commons.lang3.Validate;

public class VariableMatcher {
  public static void setupvar(TreeMaker treeMaker, List<VariableTree> apVars,
      List<LocalVariable> javaParserVars) {
    Validate.isTrue(
        apVars.size() == javaParserVars.size(),
        "collections size must be equal %d != %d (a=%s, b=%s)",
        apVars.size(), javaParserVars.size(), apVars, javaParserVars
    );
    for (int i = 0; i < apVars.size(); i++) {
      final VariableTree apVar = apVars.get(i);
      final LocalVariable javaParserVar = javaParserVars.get(i);

      final JCVariableDecl variableDecl = (JCVariableDecl) apVar;
      variableDecl.init = treeMaker.Literal(javaParserVar.getComment());
    }
  }
}
