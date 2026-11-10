package com.example.careersembed.modules.applications.domain;

import com.example.careersembed.modules.jobpostings.domain.JobPosting;
import com.example.careersembed.shared.domain.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

/** A candidate's application to a job posting. */
@Entity
@Table(name = "applications")
public class Application extends AuditedEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "job_posting_id")
  private JobPosting jobPosting;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column(name = "motivation_text", columnDefinition = "text")
  private String motivationText;

  @Column(name = "cv_file_path", nullable = false)
  private String cvFilePath;

  @Column(nullable = false)
  private ApplicationStatus status = ApplicationStatus.NEW;

  @Column(name = "submitted_at", nullable = false)
  private Instant submittedAt;

  protected Application() {}

  public Application(
      JobPosting jobPosting,
      String name,
      String email,
      String motivationText,
      String cvFilePath,
      Instant submittedAt) {
    this.jobPosting = jobPosting;
    this.name = name;
    this.email = email;
    this.motivationText = motivationText;
    this.cvFilePath = cvFilePath;
    this.submittedAt = submittedAt;
  }

  public JobPosting getJobPosting() {
    return jobPosting;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getMotivationText() {
    return motivationText;
  }

  public String getCvFilePath() {
    return cvFilePath;
  }

  public ApplicationStatus getStatus() {
    return status;
  }

  public Instant getSubmittedAt() {
    return submittedAt;
  }
}
