package com.example.careersembed.shared.web;

/** Converts Java property names to the snake_case names used in the JSON API. */
public final class SnakeCase {

  private SnakeCase() {}

  public static String of(String camelCase) {
    StringBuilder out = new StringBuilder(camelCase.length() + 4);
    for (char c : camelCase.toCharArray()) {
      if (Character.isUpperCase(c)) {
        out.append('_').append(Character.toLowerCase(c));
      } else {
        out.append(c);
      }
    }
    return out.toString();
  }
}
