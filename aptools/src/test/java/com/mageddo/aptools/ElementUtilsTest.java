package com.mageddo.aptools;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;

import com.mageddo.aptools.elements.ElementUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ElementUtilsTest {

	@Test
	void mustConvertToRightClassName(){
		// arrange
		final Element classElement = Mockito.mock(Element.class);
		Mockito.doReturn(ElementKind.CLASS).when(classElement).getKind();
		Mockito.doReturn("com.acme.UpperClass").when(classElement).toString();

		final Name innerClassName = Mockito.mock(Name.class);
		Mockito.doReturn("InnerClass").when(innerClassName).toString();

		final Element innerClassElement = Mockito.mock(Element.class);
		Mockito.doReturn(ElementKind.CLASS).when(innerClassElement).getKind();
		Mockito.doReturn(innerClassName).when(innerClassElement).getSimpleName();
		Mockito.doReturn(classElement).when(innerClassElement).getEnclosingElement();

		// act
		final String className = ElementUtils.toClassName(innerClassElement);

		// assert
		Assertions.assertEquals("com.acme.UpperClass$InnerClass", className);
	}
}
