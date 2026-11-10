package com.example.careersembed.shared.exception;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

/** A validation error for one request field that can only be detected in the service layer. */
public class FieldValidationException extends BaseException {

  private final String field;

  public FieldValidationException(String field, String messageKey, Object... args) {
    super(messageKey, HttpStatus.UNPROCESSABLE_CONTENT, args);
    this.field = field;
  }

  @Override
  public Object body(String message) {
    return new ValidationErrorResponse(message, Map.of(field, List.of(message)));
  }
}
