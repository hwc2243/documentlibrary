package com.github.hwc2243.documentlibrary.model;

import java.util.stream.Stream;

public enum DocumentObjectObjectType {
  FILE("FILE"),
  FOLDER("FOLDER");

  private final String name;

  private DocumentObjectObjectType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  // A static lookup method to find the enum by its name
  public static DocumentObjectObjectType fromValue(String name) {
    return Stream.of(DocumentObjectObjectType.values())
        .filter(type -> type.getName().equals(name))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown enum value: " + name));
  }
}