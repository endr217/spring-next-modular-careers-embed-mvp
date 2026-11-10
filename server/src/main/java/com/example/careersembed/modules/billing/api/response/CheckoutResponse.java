package com.example.careersembed.modules.billing.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Kept in camelCase to match the existing API contract. */
public record CheckoutResponse(@JsonProperty("sessionId") String sessionId, String url) {}
