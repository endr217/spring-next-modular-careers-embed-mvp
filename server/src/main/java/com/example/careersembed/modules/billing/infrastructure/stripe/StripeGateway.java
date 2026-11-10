package com.example.careersembed.modules.billing.infrastructure.stripe;

import com.example.careersembed.modules.billing.config.StripeProperties;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/** Thin wrapper over the Stripe SDK so services stay free of SDK details. */
@Component
public class StripeGateway {

  private final StripeClient client;

  public StripeGateway(StripeProperties properties) {
    this.client = new StripeClient(properties.secretKey());
  }

  public String createCustomer(String email, String name, Map<String, String> metadata)
      throws StripeException {
    Customer customer =
        client
            .v1()
            .customers()
            .create(
                CustomerCreateParams.builder()
                    .setEmail(email)
                    .setName(name)
                    .putAllMetadata(metadata)
                    .build());
    return customer.getId();
  }

  public CheckoutSession createCheckoutSession(CheckoutCommand command) throws StripeException {
    SessionCreateParams.Builder params =
        SessionCreateParams.builder()
            .setCustomer(command.customerId())
            .setMode(
                "payment".equals(command.mode())
                    ? SessionCreateParams.Mode.PAYMENT
                    : SessionCreateParams.Mode.SUBSCRIPTION)
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setPrice(command.priceId())
                    .setQuantity(command.quantity())
                    .build())
            .setSuccessUrl(command.successUrl())
            .setCancelUrl(command.cancelUrl())
            .putAllMetadata(command.metadata());
    command
        .paymentMethodTypes()
        .forEach(
            type ->
                params.addPaymentMethodType(
                    SessionCreateParams.PaymentMethodType.valueOf(type.toUpperCase())));
    Session session = client.v1().checkout().sessions().create(params.build());
    return new CheckoutSession(session.getId(), session.getUrl());
  }

  public PaidSession retrieveSession(String sessionId) throws StripeException {
    Session session =
        client
            .v1()
            .checkout()
            .sessions()
            .retrieve(
                sessionId,
                SessionRetrieveParams.builder()
                    .addExpand("subscription")
                    .addExpand("subscription.latest_invoice")
                    .build());
    Subscription subscription = session.getSubscriptionObject();
    PaidSubscription paid = null;
    if (subscription != null) {
      var item = subscription.getItems().getData().getFirst();
      paid =
          new PaidSubscription(
              subscription.getId(),
              subscription.getCustomer(),
              subscription.getStatus(),
              item.getPrice().getId(),
              item.getQuantity());
    }
    return new PaidSession(session.getPaymentStatus(), session.getCustomer(), paid);
  }

  public void cancelSubscription(String subscriptionId) throws StripeException {
    client.v1().subscriptions().cancel(subscriptionId);
  }

  public record CheckoutCommand(
      String customerId,
      String priceId,
      long quantity,
      String mode,
      List<String> paymentMethodTypes,
      String successUrl,
      String cancelUrl,
      Map<String, String> metadata) {}

  public record CheckoutSession(String id, String url) {}

  public record PaidSession(String paymentStatus, String customerId, PaidSubscription subscription) {}

  public record PaidSubscription(
      String id, String customerId, String status, String priceId, Long quantity) {}
}
