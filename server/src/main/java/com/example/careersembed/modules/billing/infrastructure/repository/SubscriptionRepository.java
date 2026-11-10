package com.example.careersembed.modules.billing.infrastructure.repository;

import com.example.careersembed.modules.billing.domain.Subscription;
import com.example.careersembed.shared.repository.BaseRepository;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends BaseRepository<Subscription, Long> {

  Optional<Subscription> findByStripeId(String stripeId);

  List<Subscription> findByUserId(Long userId);
}
