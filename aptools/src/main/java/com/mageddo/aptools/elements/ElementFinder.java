package com.mageddo.aptools.elements;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import com.mageddo.aptools.util.Predicate;

public class ElementFinder {

  private ElementFinder() {
  }

  /**
   * Find all enclosing elements which match #kind
   */
  public static List<Element> find(Element e, final ElementKind kind) {
    return find(e, new Predicate<Element>() {
      @Override
      public boolean test(Element element) {
        return element.getKind().equals(kind);
      }
    });
  }

  public static <T>List<Element> find(Element e, Predicate<Element> predicate) {
    final List<Element> elements = new ArrayList<>();
    find(e, predicate, elements);
    return elements;
  }

  private static <T>void find(Element e, Predicate<Element> predicate, List<Element> elements) {
    for (final Element enclosedElement : e.getEnclosedElements()) {
      if (predicate.test(enclosedElement)) {
        elements.add(enclosedElement);
      }
      find(enclosedElement, predicate, elements);
    }
  }

}
