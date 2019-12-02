package com.mageddo.aptools.textblock;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.mageddo.aptools.Processor;
import com.mageddo.aptools.elements.ElementFinder;
import com.mageddo.aptools.elements.ElementUtils;
import com.mageddo.aptools.log.Logger;
import com.mageddo.aptools.log.LoggerFactory;
import com.mageddo.aptools.util.Predicate;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;

import lombok.TextBlock;

public class TextBlockProcessor implements Processor {
  private final Logger logger = LoggerFactory.getLogger();
  private final ProcessingEnvironment processingEnv;
  private final Trees trees;
  private final TreeMaker treeMaker;
  private final Set<Element> classes;

  public TextBlockProcessor(ProcessingEnvironment processingEnv) {
    this.processingEnv = processingEnv;
    this.classes = new HashSet<>();
    this.trees = Trees.instance(processingEnv);
    this.treeMaker = TreeMaker.instance(((JavacProcessingEnvironment) processingEnv).getContext());
  }

  @Override
  public void process(Set<TypeElement> annotations, RoundEnvironment roundEnv) {
    try {
    logger.warn("processingover=%s, tmp: %s", roundEnv.processingOver(), roundEnv.getRootElements());
    for (final Element element : roundEnv.getRootElements()) {

      final ClassAnnotatedVariableTreePathScanner apScanner =
          new ClassAnnotatedVariableTreePathScanner(TextBlock.class);
      apScanner.scan(this.trees.getPath(element), element);

      System.out.println(apScanner.getVariables());

      ElementUtils.validateTypeElement(element);
      CompilationUnit compilationUnit = JavaParser.parse(
          ((Symbol.ClassSymbol) element).sourcefile.openReader(true), true
      );
      final ClassAnnotatedVariablesJavaParserScanner javaParserScanner =
          new ClassAnnotatedVariablesJavaParserScanner(TextBlock.class);
      compilationUnit.accept(javaParserScanner, compilationUnit);

      VariableMatcher.setupvar(this.treeMaker, apScanner.getVariables(),
          javaParserScanner.getVariables());

      this.classes.add(element);

      final List<Element> textBlockVar = ElementFinder.find(element,
          textBlockAnnotatedElementPredicate()
      );
      List<Element> childs = ElementFinder.find(element, new Predicate<Element>() {
        @Override
        public boolean test(Element element) {
          return true;
        }
      });
      for (Element child : childs) {
        System.out.printf("child=%s class=%sm childs=%s%n", child, child.getClass(),
            ElementFilter.fieldsIn(Collections.singleton(child)));
//        if(child instanceof Symbol.MethodSymbol) {
//          Symbol.MethodSymbol method = (Symbol.MethodSymbol) child;
//          System.out.println("=>" + method.loca);
//        }

      }
      javaParserMethodToExtractVars(roundEnv, (Symbol.ClassSymbol) element);
//      logger.warn("class=%s, textBlockVar=%s, children=%s", element, textBlockVar, childs);

    }


      for (Element aClass : classes) {
//          Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol) aClass;
//          System.out.println("members " + classSymbol.members());
        if(aClass.toString().contains("Person")){
//          System.out.println("deleting Fruit class");
//          classSymbol.classfile = classSymbol.sourcefile;
//          /home/typer/dev/projects/annotation-processing-tools/lombok-ext/acceptance-tests/build/classes/java/main/tmp.test
//          FileObject fileObject = processingEnv
//              .getFiler()
//              .createResource(
//                  StandardLocation.CLASS_OUTPUT, "",
//                  "tmp.test"
//              ).toUri().toString();
//          final File clazz = new File("/home/typer/dev/projects/annotation-processing-tools" +
//              "/lombok-ext/acceptance-tests/build/classes/java/main/com/mageddo/lombok/Person.class");

//          boolean deleted = clazz.delete();
//          System.out.printf("%s %s %n", clazz.exists(), deleted);

//          if(deleted) {
//            JavaFileObject fruitBSource = processingEnv
//                .getFiler()
//                .createSourceFile("com.mageddo.lombok.Person", aClass);
//            Writer writer = fruitBSource.openWriter();
//            writer.write("package com.mageddo.lombok; public class Person {}");
//            writer.close();
//          }
//          JavaFileObject fruitB = processingEnv
//          .getFiler()
//          .createClassFile("Person123");
//          System.out.println("=======" + fruitB.toUri());
//          fruitB.delete();
//          JavaFileObject fruitB = classSymbol.sourcefile;
//          fruitB.delete();
//          JavaFileObject fruitB = processingEnv
//              .getFiler()
//              .createSourceFile("Fruit", aClass);
//          JavaFileObject fruitB = ((Symbol.ClassSymbol) aClass).sourcefile;
//          if(contentBefore == null) {
//            Reader reader = fruitB.openReader(true);
//            contentBefore = IOUtils.toString(reader);
//            reader.close();
//          }
//          Writer writer = fruitB.openWriter();
//          writer.write("package com.mageddo.lombok; public class Person { " +
//              "private String lastName;" +
//          " }");
//          writer.close();
//          classSymbol.sourcefile = fruitB;
//          classSymbol.classfile = fruitB;
//      System.out.printf("writer=%s, javaFileObject=%s, %n", writer, fruitB);
//          System.out.printf("writer=%s, %n", classSymbol);
        }
      }

    if(roundEnv.processingOver()){
//      logger.warn("writing to class " + this.classes);
//      final File clazz = new File("/home/typer/dev/projects/annotation-processing-tools" +
//          "/lombok-ext/acceptance-tests/build/classes/java/main/com/mageddo/lombok/Person.class");
//
//      boolean deleted = clazz.delete();
//      System.out.printf("%s %s %n", clazz.exists(), deleted);
//
//      if(deleted) {
//        JavaFileObject fruitBSource = processingEnv
//            .getFiler()
//            .createSourceFile("com.mageddo.lombok.Person");
//        Writer writer = fruitBSource.openWriter();
//        writer.write("package com.mageddo.lombok; public class Person {}");
//        writer.close();
//      }
//      Symbol.ClassSymbol lastClassUnit = (Symbol.ClassSymbol) this.classes;
//      CompilationUnit cu = JavaParser.parse(lastClassUnit.sourcefile.openReader(true), true);
//      System.out.println("classfile: " + lastClassUnit.classfile);
//      System.out.println(cu.toString());

      for (Element aClass : classes) {
        if(aClass.toString().contains("Person")){
//          System.out.println("deleting Fruit class");
          Symbol.ClassSymbol classSymbol = (Symbol.ClassSymbol) aClass;
////          classSymbol.classfile = classSymbol.sourcefile;

//          FileObject fileObject = processingEnv
//              .getFiler()
//              .createResource(
//                  StandardLocation.CLASS_OUTPUT, "",
//                  "tmp.test"
//              );

//          JavaFileObject fruitB = processingEnv
//          .getFiler()
//          .createClassFile("Person", aClass);
//          fruitB.delete();
//          System.out.printf("created, fileObject=%s, uri=%s, classUri=%s%n", fileObject,
//              fileObject.toUri(), fruitB.toUri());
////
//          ;
////          System.out.println("=======");
//          JavaFileObject fruitBSource = processingEnv
//              .getFiler()
//              .createSourceFile("com.mageddo.lombok.Person", aClass);
//          Writer writer = fruitBSource.openWriter();
//          writer.write("package com.mageddo.lombok; public class Person {}");
//          writer.close();
//          classSymbol.sourcefile = fruitB;
////      System.out.printf("writer=%s, javaFileObject=%s, %n", writer, fruitB);
////          System.out.printf("writer=%s, %n", classSymbol);
//          Writer writer = classSymbol.sourcefile.openWriter();
//          writer.write(contentBefore);
//          writer.close();
        }
      }
//      JavaFileObject fruitB = processingEnv
//          .getFiler()
//          .createSourceFile("Fruit");
//      System.out.println("=======");
//      Writer writer = fruitB.openWriter();
//      System.out.printf("writer=%s, javaFileObject=%s, %n", writer, fruitB);
//      writer.write("package com.mageddo.lombok; public class Fruit {}");
//      writer.close();

//      Writer writer = lastClassUnit.sourcefile.openWriter();
//      writer.write(cu.toString());
//      writer.close();
    }

    }catch (Exception e){
//      logger.error(ExceptionUtils.getStackTrace(e));
      logger.error(e.getMessage());
    }
  }

  private void javaParserMethodToExtractVars(RoundEnvironment roundEnv, Symbol.ClassSymbol element) throws ParseException, IOException {
    final CompilationUnit cu = JavaParser.parse(element.sourcefile.openReader(true), true);
    recursivePrint(cu);
  }

  private void recursivePrint(Node cu) {
    for (Node childrenNode : cu.getChildrenNodes()) {
//      System.out.printf("child=%s%n", childrenNode.toString());
      recursivePrint(childrenNode);
    }
  }

  public Predicate<Element> textBlockAnnotatedElementPredicate(){
    return new Predicate<Element>(){
      @Override
      public boolean test(Element o) {
        return o.getAnnotation(TextBlock.class) != null;
      }
    };
  }
}
