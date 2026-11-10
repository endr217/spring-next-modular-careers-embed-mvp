package com.example.careersembed.modules.billing.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Kept in camelCase to match the existing API contract. */
public record PaymentVerificationResponse(
    String status,
    @JsonProperty("customerId") String customerId,
    @JsonProperty("subscriptionId") String subscriptionId) {}
