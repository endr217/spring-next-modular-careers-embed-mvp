package com.example.careersembed.modules.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/** One pending reset per e-mail; only a hash of the token is stored. */
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

  @Id private String email;

  @Column(name = "token", nullable = false)
  private String tokenHash;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected PasswordResetToken() {}

  public PasswordResetToken(String email, String tokenHash, Instant createdAt) {
    this.email = email;
    this.tokenHash = tokenHash;
    this.createdAt = createdAt;
  }

  public String getEmail() {
    return email;
  }

  public String getTokenHash() {
    return tokenHash;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void renew(String tokenHash, Instant createdAt) {
    this.tokenHash = tokenHash;
    this.createdAt = createdAt;
  }
}
