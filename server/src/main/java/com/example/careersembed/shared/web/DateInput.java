package com.example.careersembed.shared.web;

import com.example.careersembed.shared.exception.FieldValidationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** Parses optional date inputs: "2025-05-01", "2025-05-01 10:00:00" or ISO-8601. */
public final class DateInput {

  private static final DateTimeFormatter SPACED = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private DateInput() {}

  public static Instant parse(String field, String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    String trimmed = value.trim();
    try {
      return LocalDate.parse(trimmed).atStartOfDay().toInstant(ZoneOffset.UTC);
    } catch (DateTimeParseException ignored) {
      // try the next format
    }
    try {
      return LocalDateTime.parse(trimmed, SPACED).toInstant(ZoneOffset.UTC);
    } catch (DateTimeParseException ignored) {
      // try the next format
    }
    try {
      return OffsetDateTime.parse(trimmed).toInstant();
    } catch (DateTimeParseException ignored) {
      // try the next format
    }
    try {
      return LocalDateTime.parse(trimmed).toInstant(ZoneOffset.UTC);
    } catch (DateTimeParseException ex) {
      throw new FieldValidationException(field, "validation.date.invalid");
    }
  }
}
