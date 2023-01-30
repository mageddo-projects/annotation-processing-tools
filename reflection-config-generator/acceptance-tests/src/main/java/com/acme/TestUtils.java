package com.acme;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.apache.commons.io.IOUtils;

public class TestUtils {

	private TestUtils() {
	}

	public static String getResourceAsString(String path) {
		try {
			return IOUtils.toString(getResourceAsStream(path), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static InputStream getResourceAsStream(String path) {
		return TestUtils.class.getResourceAsStream(path);
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
