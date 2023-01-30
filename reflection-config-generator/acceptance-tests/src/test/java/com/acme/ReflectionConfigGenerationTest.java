package com.acme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.Test;

import static com.acme.TestUtils.getResourceAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectionConfigGenerationTest {

	private static final ObjectMapper objectMapper = new ObjectMapper()
		.enable(SerializationFeature.INDENT_OUTPUT)
		;

	@Test
	void mustConfigureReflectJson() throws JsonProcessingException {
		// arrange

		// act
		final List<JsonNode> items = readReflectConfig("reflect.json");

		// assert
		assertEquals(13, items.size());
		assertEquals(
				getResourceAsString("/reflection-config-generation-test/001.json"),
				objectMapper.writeValueAsString(items)
		);
	}

	@Test
	void mustConfigureThirdPartyReflectJson() throws JsonProcessingException {
		// arrange

		// act
		final List<JsonNode> items = readReflectConfig("reflect-third-party.json");

		// assert
		assertEquals(14, items.size());
		assertEquals(
				getResourceAsString("/reflection-config-generation-test/002.json"),
				objectMapper.writeValueAsString(items)
		);
	}


	@Test
	void mustConfigureNativeImagePropertiesFile(){
		// arrange

		// act
		final String propsContent = getResourceAsString("/META-INF/native-image/com.acme/native-image.properties");

		// assert
		assertEquals(getResourceAsString("/reflection-config-generation-test/002.properties"), propsContent);
	}


	private List<JsonNode> readReflectConfig(final String fileName) throws JsonProcessingException {
		final List<JsonNode> items = new ArrayList<>();
		final JsonNode reflectItems = this.objectMapper.readTree(
				getResourceAsString("/META-INF/native-image/com.acme/" + fileName)
		);
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

}
