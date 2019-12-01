package nativeimage.core;

import com.mageddo.aptools.ClassUtils;
import com.mageddo.aptools.elements.ElementUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ClassUtilsTest {

	@Test
	void mustFindClassPackage(){
		// arrange
		final String className = Map.class.getName();

		// act
		final String classPackage = ClassUtils.getClassPackage(className);

		// assert
		assertEquals("java.util", classPackage);
	}

	@Test
	void mustMatchInnerClass(){
		// arrange

		// act
		final boolean own = ClassUtils.doPackageOwnClass("com.acme", "com.acme.UpperClass$InnerClass");

		// assert
		assertTrue(own);
	}
}
