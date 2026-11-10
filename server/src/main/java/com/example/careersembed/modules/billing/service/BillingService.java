package com.example.careersembed.modules.billing.service;

import com.example.careersembed.config.AppProperties;
import com.example.careersembed.modules.billing.api.exception.BillingException;
import com.example.careersembed.modules.billing.api.request.CheckoutRequest;
import com.example.careersembed.modules.billing.api.response.CheckoutResponse;
import com.example.careersembed.modules.billing.api.response.PaymentVerificationResponse;
import com.example.careersembed.modules.billing.config.StripeProperties;
import com.example.careersembed.modules.billing.domain.Subscription;
import com.example.careersembed.modules.billing.infrastructure.repository.SubscriptionRepository;
import com.example.careersembed.modules.billing.infrastructure.stripe.StripeGateway;
import com.example.careersembed.modules.users.domain.User;
import com.example.careersembed.modules.users.infrastructure.repository.UserRepository;
import com.example.careersembed.modules.users.service.UserService;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import java.time.Clock;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingService {

  private static final Logger log = LoggerFactory.getLogger(BillingService.class);

  private final StripeGateway stripe;
  private final StripeProperties stripeProperties;
  private final AppProperties appProperties;
  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;
  private final UserService userService;
  private final Clock clock;

  public BillingService(
      StripeGateway stripe,
      StripeProperties stripeProperties,
      AppProperties appProperties,
      SubscriptionRepository subscriptionRepository,
      UserRepository userRepository,
      UserService userService,
      Clock clock) {
    this.stripe = stripe;
    this.stripeProperties = stripeProperties;
    this.appProperties = appProperties;
    this.subscriptionRepository = subscriptionRepository;
    this.userRepository = userRepository;
    this.userService = userService;
    this.clock = clock;
  }

  @Transactional
  public CheckoutResponse checkout(long userId, CheckoutRequest request) {
    User user = userService.get(userId);
    String mode = request.modeOrDefault();
    if ("subscription".equals(mode) && isSubscribed(userId)) {
      throw BillingException.alreadySubscribed();
    }
    if (!stripeProperties.isAllowedPrice(request.priceId())) {
      throw BillingException.invalidPrice();
    }
    try {
      if (user.getStripeId() == null) {
        user.assignStripeId(
            stripe.createCustomer(
                user.getEmail(),
                user.getName() + " " + user.getSurname(),
                Map.of("user_id", String.valueOf(userId))));
      }
      String frontend = appProperties.frontendUrl();
      StripeGateway.CheckoutSession session =
          stripe.createCheckoutSession(
              new StripeGateway.CheckoutCommand(
                  user.getStripeId(),
                  request.priceId(),
                  request.quantityOrDefault(),
                  mode,
                  request.paymentMethodTypesOrDefault(),
                  frontend + stripeProperties.successPath(),
                  frontend + stripeProperties.cancelPath(),
                  Map.of(
                      "user_id", String.valueOf(userId),
                      "plan_type", request.planTypeOrDefault())));
      return new CheckoutResponse(session.id(), session.url());
    } catch (StripeException ex) {
      log.error("Stripe checkout failed: {}", ex.getMessage());
      throw BillingException.providerError();
    }
  }

  @Transactional
  public PaymentVerificationResponse verify(long userId, String sessionId) {
    StripeGateway.PaidSession session;
    try {
      session = stripe.retrieveSession(sessionId);
    } catch (InvalidRequestException ex) {
      throw BillingException.sessionNotFound(sessionId);
    } catch (StripeException ex) {
      log.error("Stripe session lookup failed: {}", ex.getMessage());
      throw BillingException.providerError();
    }
    User user = userService.get(userId);
    if (session.customerId() == null || !session.customerId().equals(user.getStripeId())) {
      throw BillingException.sessionNotFound(sessionId);
    }
    StripeGateway.PaidSubscription paid = session.subscription();
    if (paid != null) {
      userRepository
          .findByStripeId(paid.customerId())
          .ifPresent(
              owner -> {
                Subscription subscription =
                    subscriptionRepository
                        .findByStripeId(paid.id())
                        .orElseGet(() -> new Subscription(owner.getId(), paid.id()));
                subscription.sync(paid.status(), paid.priceId(), paid.quantity());
                subscriptionRepository.save(subscription);
              });
    }
    return new PaymentVerificationResponse(
        session.paymentStatus(), session.customerId(), paid == null ? null : paid.id());
  }

  @Transactional
  public void cancel(long userId, String subscriptionId) {
    Subscription subscription =
        subscriptionRepository
            .findByStripeId(subscriptionId)
            .filter(s -> s.getUserId() == userId)
            .orElseThrow(BillingException::subscriptionNotFound);
    try {
      stripe.cancelSubscription(subscriptionId);
    } catch (StripeException ex) {
      log.error("Stripe cancellation failed: {}", ex.getMessage());
      throw BillingException.providerError();
    }
    subscription.cancel(clock.instant());
  }

  private boolean isSubscribed(long userId) {
    var now = clock.instant();
    return subscriptionRepository.findByUserId(userId).stream().anyMatch(s -> s.isActive(now));
  }
}
