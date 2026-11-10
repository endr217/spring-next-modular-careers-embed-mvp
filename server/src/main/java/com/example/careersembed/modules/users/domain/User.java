package com.example.careersembed.modules.users.domain;

import com.example.careersembed.shared.domain.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User extends AuditedEntity {

  public static final short STATUS_ACTIVE = 1;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String surname;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "email_verified_at")
  private Instant emailVerifiedAt;

  @Column(name = "password", nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private short status = STATUS_ACTIVE;

  @Column(name = "stripe_id")
  private String stripeId;

  protected User() {}

  public User(String name, String surname, String email, String passwordHash) {
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.passwordHash = passwordHash;
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }

  public String getEmail() {
    return email;
  }

  public Instant getEmailVerifiedAt() {
    return emailVerifiedAt;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public short getStatus() {
    return status;
  }

  public String getStripeId() {
    return stripeId;
  }

  public void changePassword(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public void assignStripeId(String stripeId) {
    this.stripeId = stripeId;
  }
}
