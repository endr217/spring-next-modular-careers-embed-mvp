package com.example.careersembed.modules.billing.api.controller;

import com.example.careersembed.modules.billing.api.request.CancelSubscriptionRequest;
import com.example.careersembed.modules.billing.api.request.CheckoutRequest;
import com.example.careersembed.modules.billing.api.response.CheckoutResponse;
import com.example.careersembed.modules.billing.api.response.PaymentVerificationResponse;
import com.example.careersembed.modules.billing.service.BillingService;
import com.example.careersembed.security.CurrentUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class BillingController {

  private final BillingService billingService;
  private final CurrentUser currentUser;
  private final MessageSource messages;

  public BillingController(
      BillingService billingService, CurrentUser currentUser, MessageSource messages) {
    this.billingService = billingService;
    this.currentUser = currentUser;
    this.messages = messages;
  }

  @PostMapping("/checkout")
  public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
    return billingService.checkout(currentUser.id(), request);
  }

  @GetMapping("/verify-payment")
  public PaymentVerificationResponse verify(
      @RequestParam("session_id") @NotBlank String sessionId) {
    return billingService.verify(currentUser.id(), sessionId);
  }

  @PostMapping("/cancel-subscription")
  public Map<String, String> cancel(@Valid @RequestBody CancelSubscriptionRequest request) {
    billingService.cancel(currentUser.id(), request.subscriptionId());
    return Map.of(
        "message",
        messages.getMessage("billing.subscription_cancelled", null, LocaleContextHolder.getLocale()));
  }
}
