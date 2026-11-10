package com.example.careersembed.shared.web;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/** Date formats used by the JSON API. */
public final class DateTimes {

  /** "2025-04-08 15:42:28" in UTC. */
  private static final DateTimeFormatter DATE_TIME =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);

  private DateTimes() {}

  public static String format(Instant instant) {
    return instant == null ? null : DATE_TIME.format(instant);
  }

  public static String iso(Instant instant) {
    return instant == null ? null : DateTimeFormatter.ISO_INSTANT.format(instant);
  }
}
