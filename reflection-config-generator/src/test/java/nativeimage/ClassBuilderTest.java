package nativeimage;

import nativeimage.core.TypeBuilder;
import nativeimage.vo.Pojo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sun.reflect.annotation.AnnotationParser;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

class ClassBuilderTest {

	@Test
	void mustMapScanClassName() {
		// arrange

		final Map<String, Object> params = new LinkedHashMap<>();
		params.put("scanClassName", Pojo.class.getName());
		params.put("scanClass", Set.class);

		final Reflection ann = (Reflection) AnnotationParser.annotationForMap(
			Reflection.class, params
		);

		// act
		final Set<String> classes = TypeBuilder.of(null, ann);

		// assert
		assertEquals(1, classes.size());
		assertEquals(Pojo.class.getName(), classes.iterator().next());
	}

	@Test
	void mustMapScanClass() {
		// arrange

		final Map<String, Object> params = new LinkedHashMap<>();
		params.put("scanClassName", "");
		params.put("scanClass", Pojo.class);

		final Reflection ann = (Reflection) AnnotationParser.annotationForMap(
			Reflection.class, params
		);

		// act
		final Set<String> classes = TypeBuilder.of(null, ann);

		// assert
		assertEquals(1, classes.size());
		assertEquals(Pojo.class.getName(), classes.iterator().next());
	}

	@Test
	void mustMapScanPackageClasses() {
		// arrange

		final Class expectedClass = Pojo.class;
		final Map<String, Object> params = new LinkedHashMap<>();
		params.put("scanClassName", "");
		params.put("scanClass", Void.class);
		params.put("scanPackage", "nativeimage.vo");

		final Reflection ann = (Reflection) AnnotationParser.annotationForMap(
			Reflection.class, params
		);

		final Element element = Mockito.mock(Element.class);
		doReturn(ElementKind.CLASS).when(element).getKind();
		doReturn(expectedClass.getName()).when(element).toString();

		final Name name = Mockito.mock(Name.class);
		doReturn(expectedClass.getSimpleName()).when(name).toString();
		doReturn(expectedClass.getSimpleName().length()).when(name).length();
		doReturn(name).when(element).getSimpleName();

		// act
		final Set<String> classes = TypeBuilder.of(element, ann);

		// assert
		assertEquals(1, classes.size());
		assertEquals(Pojo.class.getName(), classes.iterator().next());
	}

	@Test
	void mustMapElementClass() {
		// arrange

		final Class<Map> expectedClass = Map.class;

		final Map<String, Object> params = new LinkedHashMap<>();
		params.put("scanClassName", "");
		params.put("scanClass", Void.class);
		params.put("scanPackage", "");

		final Reflection ann = (Reflection) AnnotationParser.annotationForMap(
			Reflection.class, params
		);

		final Element element = Mockito.mock(Element.class);
		doReturn(ElementKind.CLASS).when(element).getKind();
		doReturn(expectedClass.getName()).when(element).toString();

		// act
		final Set<String> classes = TypeBuilder.of(element, ann);

		// assert
		assertEquals(1, classes.size());
		assertEquals(expectedClass.getName(), classes.iterator().next());
	}

}
