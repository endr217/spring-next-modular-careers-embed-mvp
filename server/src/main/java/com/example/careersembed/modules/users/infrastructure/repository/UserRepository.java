package com.example.careersembed.modules.users.infrastructure.repository;

import com.example.careersembed.modules.users.domain.User;
import com.example.careersembed.shared.repository.BaseRepository;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByStripeId(String stripeId);

  boolean existsByEmail(String email);
}
