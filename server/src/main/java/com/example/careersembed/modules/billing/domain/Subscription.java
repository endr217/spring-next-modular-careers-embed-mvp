package com.example.careersembed.modules.billing.domain;

import com.example.careersembed.shared.domain.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Set;

/** Local copy of a Stripe subscription. */
@Entity
@Table(name = "subscriptions")
public class Subscription extends AuditedEntity {

  private static final Set<String> ACTIVE_STATUSES = Set.of("active", "trialing");

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false)
  private String type = "default";

  @Column(name = "stripe_id", nullable = false, unique = true)
  private String stripeId;

  @Column(name = "stripe_status", nullable = false)
  private String stripeStatus;

  @Column(name = "stripe_price")
  private String stripePrice;

  private Long quantity;

  @Column(name = "ends_at")
  private Instant endsAt;

  protected Subscription() {}

  public Subscription(Long userId, String stripeId) {
    this.userId = userId;
    this.stripeId = stripeId;
  }

  public void sync(String status, String price, Long quantity) {
    this.stripeStatus = status;
    this.stripePrice = price;
    this.quantity = quantity;
    this.endsAt = null;
  }

  public void cancel(Instant at) {
    this.stripeStatus = "canceled";
    this.endsAt = at;
  }

  public boolean isActive(Instant now) {
    return ACTIVE_STATUSES.contains(stripeStatus) && (endsAt == null || endsAt.isAfter(now));
  }

  public Long getUserId() {
    return userId;
  }

  public String getStripeId() {
    return stripeId;
  }
}
