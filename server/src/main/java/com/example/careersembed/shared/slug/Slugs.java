package com.example.careersembed.shared.slug;

import java.text.Normalizer;
import java.util.Locale;
import java.util.function.Predicate;

/** URL slug helpers. */
public final class Slugs {

  private Slugs() {}

  /** "Senior Java Developer!" becomes "senior-java-developer". */
  public static String of(String value) {
    String ascii =
        Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    String slug =
        ascii
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9\\s-]", "")
            .trim()
            .replaceAll("[\\s-]+", "-")
            .replaceAll("^-|-$", "");
    return slug.isEmpty() ? "item" : slug;
  }

  /** Appends 1, 2, 3... to the base until the slug is free. */
  public static String unique(String base, Predicate<String> exists) {
    if (!exists.test(base)) {
      return base;
    }
    int counter = 1;
    String candidate;
    do {
      candidate = base + counter++;
    } while (exists.test(candidate));
    return candidate;
  }
}
