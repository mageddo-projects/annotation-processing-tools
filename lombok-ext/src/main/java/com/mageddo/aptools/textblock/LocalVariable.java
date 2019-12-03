package com.mageddo.aptools.textblock;

import java.util.List;

public class LocalVariable {

  private String name;
  private String comment;
  private List<String> annotations;

  public String getName() {
    return name;
  }

  public LocalVariable setName(String name) {
    this.name = name;
    return this;
  }

  public String getComment() {
    return comment;
  }

  public LocalVariable setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public List<String> getAnnotations() {
    return annotations;
  }

  public LocalVariable setAnnotations(List<String> annotations) {
    this.annotations = annotations;
    return this;
  }

  @Override
  public String toString() {
    return "LocalVariable{" +
        "name='" + name + '\'' +
        ", comment='" + comment + '\'' +
        ", annotations=" + annotations +
        '}';
  }
}
