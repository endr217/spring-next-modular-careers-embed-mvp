package com.example.careersembed.modules.billing.api.request;

import jakarta.validation.constraints.NotBlank;

public record CancelSubscriptionRequest(
    @NotBlank(message = "{validation.subscription_id.required}") String subscriptionId) {}
