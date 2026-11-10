package com.example.careersembed.modules.companies.domain;

import com.example.careersembed.shared.domain.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/** Membership of a user in a company (the company_user pivot table). */
@Entity
@Table(name = "company_user")
public class CompanyMember extends AuditedEntity {

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "company_id")
  private Company company;

  @Column(nullable = false)
  private CompanyRole role;

  protected CompanyMember() {}

  public CompanyMember(Long userId, Company company, CompanyRole role) {
    this.userId = userId;
    this.company = company;
    this.role = role;
  }

  public Long getUserId() {
    return userId;
  }

  public Company getCompany() {
    return company;
  }

  public CompanyRole getRole() {
    return role;
  }
}
