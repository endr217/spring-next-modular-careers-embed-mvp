package com.example.careersembed.modules.auth.api.controller;

import com.example.careersembed.modules.auth.api.request.ForgotPasswordRequest;
import com.example.careersembed.modules.auth.api.request.LoginRequest;
import com.example.careersembed.modules.auth.api.request.RegisterRequest;
import com.example.careersembed.modules.auth.api.request.ResetPasswordRequest;
import com.example.careersembed.modules.auth.api.response.AuthResponse;
import com.example.careersembed.modules.auth.api.response.PasswordResetResponse;
import com.example.careersembed.modules.auth.domain.PasswordResetStatus;
import com.example.careersembed.modules.auth.service.AuthService;
import com.example.careersembed.modules.auth.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final PasswordResetService passwordResetService;
  private final MessageSource messages;

  public AuthController(
      AuthService authService, PasswordResetService passwordResetService, MessageSource messages) {
    this.authService = authService;
    this.passwordResetService = passwordResetService;
    this.messages = messages;
  }

  @PostMapping("/register")
  public AuthResponse register(
      @Valid @RequestBody RegisterRequest request, HttpServletRequest http) {
    return authService.register(request, http.getRemoteAddr());
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(
      @Valid @RequestBody LoginRequest request, HttpServletRequest http) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(authService.login(request, http.getRemoteAddr()));
  }

  /** Tokens are stateless JWTs; the client discards its token. */
  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/forgot-password")
  public PasswordResetResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    return toResponse(passwordResetService.sendResetLink(request.email()));
  }

  @PostMapping("/reset-password")
  public PasswordResetResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    return toResponse(passwordResetService.resetPassword(request));
  }

  private PasswordResetResponse toResponse(PasswordResetStatus status) {
    String message = messages.getMessage(status.messageKey(), null, LocaleContextHolder.getLocale());
    return new PasswordResetResponse(message, status.isSuccess(), status.value());
  }
}
