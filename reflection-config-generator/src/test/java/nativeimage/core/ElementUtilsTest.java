package nativeimage.core;

import com.mageddo.aptools.elements.ElementUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ElementUtilsTest {

	@Test
	void mustConvertToRightClassName(){
		// arrange
		final Element classElement = Mockito.mock(Element.class);
		doReturn(ElementKind.CLASS).when(classElement).getKind();
		doReturn("com.acme.UpperClass").when(classElement).toString();

		final Name innerClassName = mock(Name.class);
		doReturn("InnerClass").when(innerClassName).toString();

		final Element innerClassElement = Mockito.mock(Element.class);
		doReturn(ElementKind.CLASS).when(innerClassElement).getKind();
		doReturn(innerClassName).when(innerClassElement).getSimpleName();
		doReturn(classElement).when(innerClassElement).getEnclosingElement();

		// act
		final String className = ElementUtils.toClassName(innerClassElement);

		// assert
		assertEquals("com.acme.UpperClass$InnerClass", className);
	}
}
