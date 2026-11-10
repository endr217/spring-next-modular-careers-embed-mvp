package com.example.careersembed.shared.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

/**
 * Base class for business errors. The message is an i18n key resolved by {@link
 * GlobalExceptionHandler}; subclasses may override {@link #body(String)} to keep a specific
 * response shape.
 */
public abstract class BaseException extends RuntimeException {

  private final HttpStatus status;
  private final transient Object[] args;

  protected BaseException(String messageKey, HttpStatus status, Object... args) {
    super(messageKey);
    this.status = status;
    this.args = args;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getMessageKey() {
    return getMessage();
  }

  public Object[] getArgs() {
    return args.clone();
  }

  public Object body(String message) {
    return Map.of("message", message);
  }
}
