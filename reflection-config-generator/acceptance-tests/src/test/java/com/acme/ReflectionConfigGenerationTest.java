package com.acme;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.acme.TestUtils.getResourceAsString;
import static com.acme.TestUtils.sortJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectionConfigGenerationTest {

	private final ObjectMapper objectMapper = new ObjectMapper()
		.enable(SerializationFeature.INDENT_OUTPUT)
		;

	@Test
	void mustConfigureReflectJson() throws JsonProcessingException {
		// arrange

		// act
		final List<JsonNode> items = readReflectConfig();

		// assert
		assertEquals(10, items.size());
		assertEquals(getResourceAsString("/reflection-config-generation-test/001.json"), objectMapper.writeValueAsString(items));
	}

	private List<JsonNode> readReflectConfig() throws JsonProcessingException {
		List<JsonNode> items = new ArrayList<>();
		final JsonNode reflectItems = objectMapper.readTree(getResourceAsString("/META-INF/native-image/com.acme/reflect.json"));
		for (JsonNode jsonNode : reflectItems) {
			items.add(jsonNode);
		}
		Collections.sort(items, new Comparator<JsonNode>() {
			@Override
			public int compare(JsonNode a, JsonNode b) {
				return a.at("/name").asText().compareTo(b.at("/name").asText());
			}
		});
		return items;
	}

	@Test
	void mustConfigureNativeImagePropertiesFile(){
		// arrange

		// act
		final String propsContent = getResourceAsString("/META-INF/native-image/com.acme/native-image.properties");

		// assert
		assertEquals(getResourceAsString("/reflection-config-generation-test/002.properties"), propsContent);
	}
}
