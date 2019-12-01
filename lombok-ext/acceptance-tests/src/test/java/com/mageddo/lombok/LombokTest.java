package com.mageddo.lombok;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LombokTest {

  @Test
  void ensureLombokConfiguration() throws NoSuchMethodException {
    // arrange

    // act
    final Method fruitNameMethod = Fruit.class.getDeclaredMethod("getFruitName");

    // assert
    assertTrue(Modifier.isProtected(fruitNameMethod.getModifiers()));
  }
}
