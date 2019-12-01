package com.acme;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class TestUtils {

	private TestUtils() {
	}

	public static String getResourceAsString(String path) {
		try {
			return IOUtils.toString(TestUtils.class.getResourceAsStream(path), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String sortJson(String json){
		try {
		final ObjectMapper om = new ObjectMapper()
			.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
			.enable(SerializationFeature.INDENT_OUTPUT)
			;
			return om.writeValueAsString(om.readTree(json));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
