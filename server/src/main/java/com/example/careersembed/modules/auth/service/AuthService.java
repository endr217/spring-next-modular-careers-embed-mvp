package com.example.careersembed.modules.auth.service;

import com.example.careersembed.modules.auth.api.request.LoginRequest;
import com.example.careersembed.modules.auth.api.request.RegisterRequest;
import com.example.careersembed.modules.auth.api.response.AuthResponse;
import com.example.careersembed.modules.users.api.response.UserResponse;
import com.example.careersembed.modules.users.domain.User;
import com.example.careersembed.modules.users.infrastructure.repository.UserRepository;
import com.example.careersembed.shared.exception.FieldValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private static final Logger log = LoggerFactory.getLogger(AuthService.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final LoginRateLimiter rateLimiter;

  public AuthService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      TokenService tokenService,
      LoginRateLimiter rateLimiter) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
    this.rateLimiter = rateLimiter;
  }

  @Transactional
  public AuthResponse register(RegisterRequest request, String ip) {
    String email = request.email().trim();
    if (userRepository.existsByEmail(email)) {
      throw new FieldValidationException("email", "validation.email.taken");
    }
    User user =
        userRepository.save(
            new User(
                request.name().trim(),
                request.surname().trim(),
                email,
                passwordEncoder.encode(request.password())));
    log.info("User {} registered from {}", user.getId(), ip);
    return new AuthResponse(UserResponse.from(user), tokenService.issue(user));
  }

  @Transactional(readOnly = true)
  public AuthResponse login(LoginRequest request, String ip) {
    String email = request.email().trim();
    long wait = rateLimiter.secondsUntilAllowed(email, ip);
    if (wait > 0) {
      throw new FieldValidationException("email", "auth.throttle", wait);
    }
    User user =
        userRepository
            .findByEmail(email)
            .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
            .orElse(null);
    if (user == null) {
      rateLimiter.hit(email, ip);
      throw new FieldValidationException("email", "auth.failed");
    }
    rateLimiter.clear(email, ip);
    log.info("User {} logged in from {}", user.getId(), ip);
    return new AuthResponse(UserResponse.from(user), tokenService.issue(user));
  }
}
