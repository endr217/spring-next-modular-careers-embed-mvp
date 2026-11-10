package com.example.careersembed.modules.applications.api.response;

import com.example.careersembed.modules.applications.domain.Application;
import com.example.careersembed.shared.web.DateTimes;

public record ApplicationResponse(
    Long id,
    Long jobPostingId,
    String name,
    String email,
    String motivationText,
    String cvFilePath,
    String status,
    String submittedAt,
    ApplicationJobPosting jobPosting) {

  public record ApplicationJobPosting(
      Long id, String title, String slug, String companyName, String companyEmail) {}

  public static ApplicationResponse from(Application application) {
    var posting = application.getJobPosting();
    var company = posting.getWebsite().getCompany();
    return new ApplicationResponse(
        application.getId(),
        posting.getId(),
        application.getName(),
        application.getEmail(),
        application.getMotivationText(),
        application.getCvFilePath(),
        application.getStatus().value(),
        DateTimes.format(application.getSubmittedAt()),
        new ApplicationJobPosting(
            posting.getId(),
            posting.getTitle(),
            posting.getSlug(),
            company.getName(),
            company.getEmail()));
  }
}
