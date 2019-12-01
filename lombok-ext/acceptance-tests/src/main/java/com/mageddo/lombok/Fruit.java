package com.mageddo.lombok;

import lombok.AccessLevel;
import lombok.Getter;
import nativeimage.Reflection;

@Reflection
@Getter(AccessLevel.PROTECTED)
public class Fruit {
  private String fruitName;
}
