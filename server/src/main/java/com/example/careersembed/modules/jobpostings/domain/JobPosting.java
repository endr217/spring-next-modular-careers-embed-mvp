package com.example.careersembed.modules.jobpostings.domain;

import com.example.careersembed.modules.websites.domain.Website;
import com.example.careersembed.shared.domain.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "job_postings")
public class JobPosting extends AuditedEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "website_id")
  private Website website;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String location;

  @Column(name = "job_type", nullable = false)
  private JobType jobType;

  @Column(name = "salary_from")
  private Integer salaryFrom;

  @Column(name = "salary_to")
  private Integer salaryTo;

  @Column(nullable = false, columnDefinition = "text")
  private String description;

  @Column(name = "is_active", nullable = false)
  private boolean active = true;

  @Column(nullable = false, unique = true)
  private String slug;

  @Column(name = "published_at", nullable = false)
  private Instant publishedAt;

  @Column(name = "expires_at")
  private Instant expiresAt;

  protected JobPosting() {}

  public JobPosting(Website website, String slug, Instant publishedAt) {
    this.website = website;
    this.slug = slug;
    this.publishedAt = publishedAt;
  }

  /** Replaces the editable fields. */
  public void update(
      String title,
      String location,
      JobType jobType,
      Integer salaryFrom,
      Integer salaryTo,
      String description,
      Instant expiresAt,
      boolean active) {
    this.title = title;
    this.location = location;
    this.jobType = jobType;
    this.salaryFrom = salaryFrom;
    this.salaryTo = salaryTo;
    this.description = description;
    this.expiresAt = expiresAt;
    this.active = active;
  }

  public Website getWebsite() {
    return website;
  }

  public String getTitle() {
    return title;
  }

  public String getLocation() {
    return location;
  }

  public JobType getJobType() {
    return jobType;
  }

  public Integer getSalaryFrom() {
    return salaryFrom;
  }

  public Integer getSalaryTo() {
    return salaryTo;
  }

  public String getDescription() {
    return description;
  }

  public boolean isActive() {
    return active;
  }

  public String getSlug() {
    return slug;
  }

  public Instant getPublishedAt() {
    return publishedAt;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }
}
