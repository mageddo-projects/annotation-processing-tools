package com.mageddo.lombok;

import java.net.URL;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReflectionConfigGenerationTest {
  @Test
  void ensureGRCGConfiguration(){
    // arrange

    // act
    final URL resource = getClass().getResource(
        "/META-INF/native-image/com.mageddo.lombok/reflect.json"
    );

    // assert
    assertNotNull(resource);
  }
}
