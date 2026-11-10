package com.example.careersembed.shared.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

  public ResourceNotFoundException(String messageKey, Object... args) {
    super(messageKey, HttpStatus.NOT_FOUND, args);
  }
}
