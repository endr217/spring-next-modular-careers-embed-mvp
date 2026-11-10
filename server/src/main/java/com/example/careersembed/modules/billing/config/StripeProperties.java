package com.example.careersembed.modules.billing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @param secretKey Stripe secret API key
 * @param priceMonthly price id of the monthly plan
 * @param priceYearly price id of the yearly plan
 * @param successPath dashboard path Stripe redirects to after payment
 * @param cancelPath dashboard path Stripe redirects to when the payment is cancelled
 */
@ConfigurationProperties(prefix = "app.stripe")
public record StripeProperties(
    String secretKey,
    String priceMonthly,
    String priceYearly,
    String successPath,
    String cancelPath) {

  public boolean isAllowedPrice(String priceId) {
    return priceId != null && (priceId.equals(priceMonthly) || priceId.equals(priceYearly));
  }
}
