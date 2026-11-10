package com.example.careersembed.modules.auth.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/** Allows 5 failed logins per e-mail and IP within a minute (in memory, per instance). */
@Component
public class LoginRateLimiter {

  private static final int MAX_ATTEMPTS = 5;
  private static final Duration WINDOW = Duration.ofMinutes(1);

  private final Map<String, Attempts> attempts = new ConcurrentHashMap<>();
  private final Clock clock;

  public LoginRateLimiter(Clock clock) {
    this.clock = clock;
  }

  /** Seconds until the next attempt is allowed, or 0 when not limited. */
  public long secondsUntilAllowed(String email, String ip) {
    Attempts current = attempts.get(key(email, ip));
    if (current == null || current.count() < MAX_ATTEMPTS) {
      return 0;
    }
    long seconds = Duration.between(clock.instant(), current.resetsAt()).toSeconds();
    if (seconds <= 0) {
      attempts.remove(key(email, ip));
      return 0;
    }
    return seconds;
  }

  public void hit(String email, String ip) {
    Instant now = clock.instant();
    attempts.compute(
        key(email, ip),
        (k, current) ->
            current == null || now.isAfter(current.resetsAt())
                ? new Attempts(1, now.plus(WINDOW))
                : new Attempts(current.count() + 1, current.resetsAt()));
  }

  public void clear(String email, String ip) {
    attempts.remove(key(email, ip));
  }

  private static String key(String email, String ip) {
    return email.toLowerCase(Locale.ROOT) + "|" + ip;
  }

  private record Attempts(int count, Instant resetsAt) {}
}
