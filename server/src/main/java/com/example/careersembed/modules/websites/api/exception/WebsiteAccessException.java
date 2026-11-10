package com.example.careersembed.modules.websites.api.exception;

import com.example.careersembed.shared.exception.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

/** Website registration errors, all answered with 403 and an {error, message} body. */
public class WebsiteAccessException extends BaseException {

  private WebsiteAccessException(String messageKey) {
    super(messageKey, HttpStatus.FORBIDDEN);
  }

  public static WebsiteAccessException userNotAuthorized() {
    return new WebsiteAccessException("website.user_not_authorized");
  }

  public static WebsiteAccessException domainAlreadyExists() {
    return new WebsiteAccessException("website.domain_already_exists");
  }

  public static WebsiteAccessException websiteNotFound() {
    return new WebsiteAccessException("website.not_found");
  }

  @Override
  public Object body(String message) {
    return Map.of("error", true, "message", message);
  }
}
