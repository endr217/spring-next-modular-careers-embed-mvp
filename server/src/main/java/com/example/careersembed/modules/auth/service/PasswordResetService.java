package com.example.careersembed.modules.auth.service;

import com.example.careersembed.config.AppProperties;
import com.example.careersembed.modules.auth.api.request.ResetPasswordRequest;
import com.example.careersembed.modules.auth.domain.PasswordResetStatus;
import com.example.careersembed.modules.auth.domain.PasswordResetToken;
import com.example.careersembed.modules.auth.infrastructure.repository.PasswordResetTokenRepository;
import com.example.careersembed.modules.users.domain.User;
import com.example.careersembed.modules.users.infrastructure.repository.UserRepository;
import com.example.careersembed.shared.exception.FieldValidationException;
import com.example.careersembed.shared.mail.MailMessage;
import com.example.careersembed.shared.mail.MailService;
import com.example.careersembed.shared.web.Html;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetService {

  private static final Duration EXPIRES_AFTER = Duration.ofMinutes(60);
  private static final Duration THROTTLE = Duration.ofSeconds(60);
  private static final SecureRandom RANDOM = new SecureRandom();

  private final UserRepository userRepository;
  private final PasswordResetTokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final MailService mailService;
  private final MessageSource messages;
  private final AppProperties appProperties;
  private final Clock clock;

  public PasswordResetService(
      UserRepository userRepository,
      PasswordResetTokenRepository tokenRepository,
      PasswordEncoder passwordEncoder,
      MailService mailService,
      MessageSource messages,
      AppProperties appProperties,
      Clock clock) {
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.mailService = mailService;
    this.messages = messages;
    this.appProperties = appProperties;
    this.clock = clock;
  }

  @Transactional
  public PasswordResetStatus sendResetLink(String rawEmail) {
    String email = rawEmail.trim();
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new FieldValidationException("email", "validation.email.unknown"));
    Instant now = clock.instant();
    Optional<PasswordResetToken> existing = tokenRepository.findById(email);
    if (existing.isPresent() && existing.get().getCreatedAt().plus(THROTTLE).isAfter(now)) {
      return PasswordResetStatus.THROTTLED;
    }
    String token = newToken();
    String hash = passwordEncoder.encode(token);
    existing.ifPresentOrElse(
        t -> t.renew(hash, now), () -> tokenRepository.save(new PasswordResetToken(email, hash, now)));
    mailService.send(resetMail(user, token));
    return PasswordResetStatus.SENT;
  }

  @Transactional
  public PasswordResetStatus resetPassword(ResetPasswordRequest request) {
    String email = request.email().trim();
    Optional<User> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      return PasswordResetStatus.INVALID_USER;
    }
    Optional<PasswordResetToken> stored = tokenRepository.findById(email);
    boolean valid =
        stored.isPresent()
            && stored.get().getCreatedAt().plus(EXPIRES_AFTER).isAfter(clock.instant())
            && passwordEncoder.matches(request.token(), stored.get().getTokenHash());
    if (!valid) {
      return PasswordResetStatus.INVALID_TOKEN;
    }
    user.get().changePassword(passwordEncoder.encode(request.password()));
    tokenRepository.delete(stored.get());
    return PasswordResetStatus.RESET;
  }

  private MailMessage resetMail(User user, String token) {
    String url =
        appProperties.frontendUrl()
            + "/reset-password?token="
            + token
            + "&email="
            + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);
    String hello = msg("mail.reset.hello", user.getName());
    String line1 = msg("mail.reset.line1");
    String action = msg("mail.reset.action");
    String line2 = msg("mail.reset.line2");
    String line3 = msg("mail.reset.line3");
    String html =
        "<p>"
            + Html.escape(hello)
            + "</p><p>"
            + Html.escape(line1)
            + "</p><p><a href=\""
            + Html.escape(url)
            + "\">"
            + Html.escape(action)
            + "</a></p><p>"
            + Html.escape(line2)
            + "</p><p>"
            + Html.escape(line3)
            + "</p>";
    String text = String.join("\n\n", hello, line1, url, line2, line3);
    return new MailMessage(
        user.getEmail(), user.getName(), msg("mail.reset.subject"), text, html);
  }

  private String msg(String key, Object... args) {
    return messages.getMessage(key, args, LocaleContextHolder.getLocale());
  }

  private static String newToken() {
    byte[] bytes = new byte[32];
    RANDOM.nextBytes(bytes);
    return HexFormat.of().formatHex(bytes);
  }
}
