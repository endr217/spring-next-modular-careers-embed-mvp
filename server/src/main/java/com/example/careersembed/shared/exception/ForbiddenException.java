package com.example.careersembed.shared.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {

  public ForbiddenException() {
    super("error.forbidden", HttpStatus.FORBIDDEN);
  }

  protected ForbiddenException(String messageKey, Object... args) {
    super(messageKey, HttpStatus.FORBIDDEN, args);
  }
}
