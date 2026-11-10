package com.example.careersembed.shared.exception;

import com.example.careersembed.shared.web.SnakeCase;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  private final MessageSource messageSource;

  public GlobalExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(BaseException.class)
  ResponseEntity<Object> handleBase(BaseException ex) {
    String message = resolve(ex.getMessageKey(), ex.getArgs());
    return ResponseEntity.status(ex.getStatus()).body(ex.body(message));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  ResponseEntity<Object> handleBadParameter(MethodArgumentTypeMismatchException ex) {
    return message(HttpStatus.NOT_FOUND, "error.http.404");
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex) {
    log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
    return message(HttpStatus.CONFLICT, "error.data_conflict");
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<Object> handleUnexpected(Exception ex) {
    log.error("Unexpected error", ex);
    return message(HttpStatus.INTERNAL_SERVER_ERROR, "error.internal");
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    Map<String, List<String>> errors = new LinkedHashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error ->
                errors
                    .computeIfAbsent(SnakeCase.of(error.getField()), k -> new ArrayList<>())
                    .add(error.getDefaultMessage()));
    String summary =
        errors.values().stream()
            .flatMap(List::stream)
            .findFirst()
            .orElseGet(() -> resolve("error.validation"));
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
        .headers(headers)
        .body(new ValidationErrorResponse(summary, errors));
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String message =
        messageSource.getMessage(
            "error.http." + status.value(),
            null,
            resolve("error.request"),
            LocaleContextHolder.getLocale());
    return ResponseEntity.status(status).headers(headers).body(Map.of("message", message));
  }

  private ResponseEntity<Object> message(HttpStatus status, String key) {
    return ResponseEntity.status(status).body(Map.of("message", resolve(key)));
  }

  private String resolve(String key, Object... args) {
    return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
  }
}
