package com.example.careersembed.modules.auth.domain;

public enum PasswordResetStatus {
  SENT,
  RESET,
  INVALID_USER,
  INVALID_TOKEN,
  THROTTLED,
  ERROR;

  public String value() {
    return name().toLowerCase();
  }

  public String messageKey() {
    return "passwords." + value();
  }

  public boolean isSuccess() {
    return this == SENT || this == RESET;
  }
}
