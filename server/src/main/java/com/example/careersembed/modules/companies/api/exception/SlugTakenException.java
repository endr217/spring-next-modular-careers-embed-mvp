package com.example.careersembed.modules.companies.api.exception;

import com.example.careersembed.shared.exception.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class SlugTakenException extends BaseException {

  private final String suggestedSlug;

  public SlugTakenException(String slug, String suggestedSlug) {
    super("company.slug_taken", HttpStatus.UNPROCESSABLE_CONTENT, slug, suggestedSlug);
    this.suggestedSlug = suggestedSlug;
  }

  @Override
  public Object body(String message) {
    return Map.of(
        "success",
        false,
        "error",
        Map.of(
            "code", getStatus().value(), "message", message, "suggested_slug", suggestedSlug));
  }
}
