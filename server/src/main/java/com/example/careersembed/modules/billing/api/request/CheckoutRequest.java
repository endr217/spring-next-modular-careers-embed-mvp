package com.example.careersembed.modules.billing.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record CheckoutRequest(
    @NotBlank(message = "{validation.price_id.required}")
        @Pattern(regexp = "^price_.+", message = "{validation.price_id.invalid}")
        String priceId,
    @Min(value = 1, message = "{validation.quantity.min}") Integer quantity,
    @Pattern(regexp = "^(default|premium|enterprise)$", message = "{validation.plan_type.invalid}")
        String planType,
    @Pattern(regexp = "^(subscription|payment)$", message = "{validation.mode.invalid}")
        String mode,
    List<
            @Pattern(
                regexp = "^(card|sepa_debit)$",
                message = "{validation.payment_method_types.invalid}")
            String>
        paymentMethodTypes) {

  public long quantityOrDefault() {
    return quantity == null ? 1 : quantity;
  }

  public String planTypeOrDefault() {
    return planType == null ? "default" : planType;
  }

  public String modeOrDefault() {
    return mode == null ? "subscription" : mode;
  }

  public List<String> paymentMethodTypesOrDefault() {
    return paymentMethodTypes == null || paymentMethodTypes.isEmpty()
        ? List.of("card")
        : paymentMethodTypes;
  }
}
