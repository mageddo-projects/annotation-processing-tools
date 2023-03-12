package com.mageddo.aptools.elements;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import org.apache.commons.lang3.Validate;

public class ElementUtils {

  private static final Set<ElementKind> TYPE_ELEMENTS = new HashSet<>(Arrays.asList(
      ElementKind.CLASS, ElementKind.INTERFACE, ElementKind.ENUM
  ));

  private ElementUtils() {
  }

  public static String toClassName(Element element) {
    validateTypeElement(element);
    if (element.getEnclosingElement() == null) {
      return element.toString();
    }
    if (isTypeElement(element.getEnclosingElement())) {
      return String
          .format("%s\u0024%s", element.getEnclosingElement().toString(), element.getSimpleName());
    }
    return element.toString();
  }

  public static void validateTypeElement(Element element) {
    Validate.isTrue(
        isTypeElement(element),
        "element type must be class but is %s", element.getKind()
    );
  }

  private static boolean isTypeElement(Element e) {
    return TYPE_ELEMENTS.contains(e.getKind());
  }

	public static boolean isEquals(Element element, String className) {
		return Objects.equals(ElementUtils.toClassName(element), className);
	}

	public static boolean isNotTypeElement(Element element) {
		return !isTypeElement(element);
	}
}
