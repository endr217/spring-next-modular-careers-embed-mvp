package com.example.careersembed.modules.embed.domain;

import java.util.Arrays;
import java.util.Optional;

/** How the embed script tag is loaded by the browser. */
public enum LoadStrategy {
  DEFER("defer"),
  ASYNC("async"),
  NONE("none");

  private final String value;

  LoadStrategy(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  public boolean rendersAttribute() {
    return this != NONE;
  }

  public static Optional<LoadStrategy> fromValue(String value) {
    return Arrays.stream(values()).filter(s -> s.value.equals(value)).findFirst();
  }
}
