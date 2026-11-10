package com.example.careersembed.modules.billing.api.exception;

import com.example.careersembed.shared.exception.BaseException;
import java.util.Map;
import org.springframework.http.HttpStatus;

/** Checkout errors answered with a {success: false, error: {code, message}} body. */
public class BillingException extends BaseException {

  private BillingException(String messageKey, HttpStatus status, Object... args) {
    super(messageKey, status, args);
  }

  public static BillingException invalidPrice() {
    return new BillingException("billing.invalid_price", HttpStatus.BAD_REQUEST);
  }

  public static BillingException alreadySubscribed() {
    return new BillingException("billing.already_subscribed", HttpStatus.UNPROCESSABLE_CONTENT);
  }

  public static BillingException sessionNotFound(String sessionId) {
    return new BillingException("billing.session_not_found", HttpStatus.NOT_FOUND, sessionId);
  }

  public static BillingException subscriptionNotFound() {
    return new BillingException("billing.subscription_not_found", HttpStatus.NOT_FOUND);
  }

  public static BillingException providerError() {
    return new BillingException("billing.provider_error", HttpStatus.BAD_GATEWAY);
  }

  @Override
  public Object body(String message) {
    return Map.of(
        "success", false, "error", Map.of("code", getStatus().value(), "message", message));
  }
}
