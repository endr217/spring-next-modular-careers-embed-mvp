package com.example.careersembed.modules.companies.domain;

import com.example.careersembed.shared.domain.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "companies")
public class Company extends AuditedEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String slug;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "logo_path")
  private String logoPath;

  protected Company() {}

  public Company(String name, String slug, String email) {
    this.name = name;
    this.slug = slug;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getSlug() {
    return slug;
  }

  public String getEmail() {
    return email;
  }

  public String getLogoPath() {
    return logoPath;
  }
}
