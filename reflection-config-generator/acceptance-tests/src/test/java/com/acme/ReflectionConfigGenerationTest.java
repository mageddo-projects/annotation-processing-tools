package com.acme;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static com.acme.TestUtils.getResourceAsStream;
import static com.acme.TestUtils.getResourceAsString;
import nativeimage.core.thirdparty.Main;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectionConfigGenerationTest {

	private static final ObjectMapper objectMapper = new ObjectMapper()
		.enable(SerializationFeature.INDENT_OUTPUT)
		;

	@Test
	void mustConfigureReflectJson() throws IOException {
		// arrange
		// act
		// assert
		assertEquals(
				getResourceAsString("/reflection-config-generation-test/001.json"),
				readReflectConfig("reflect.json")
		);
	}

	@Test
	void mustConfigureThirdPartyReflectJson(@TempDir Path tmpDir) throws Exception {
		// arrange
		final Path reflectFile = Files.createTempFile(tmpDir, "reflect", ".json");

		// act
		Main.main(new String[]{
				"com.fasterxml.jackson.core.util", reflectFile.toString()
		});

		// assert
		assertEquals(
				readReflectConfig(getResourceAsStream("/reflection-config-generation-test/002.json")),
				readReflectConfig(Files.newInputStream(reflectFile))
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


	private String readReflectConfig(final String fileName) throws IOException {
		return readReflectConfig(getResourceAsStream("/META-INF/native-image/com.acme/" + fileName));
	}

	private String readReflectConfig(final InputStream in) throws IOException {
		final List<JsonNode> items = new ArrayList<>();
		final JsonNode reflectItems = this.objectMapper.readTree(in);
		for (JsonNode jsonNode : reflectItems) {
			items.add(jsonNode);
		}
		Collections.sort(items, new Comparator<JsonNode>() {
			@Override
			public int compare(JsonNode a, JsonNode b) {
				return a.at("/name").asText().compareTo(b.at("/name").asText());
			}
		});
		return objectMapper.writeValueAsString(items);
	}

}
