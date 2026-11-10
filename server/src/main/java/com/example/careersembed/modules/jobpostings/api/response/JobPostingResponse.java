package com.example.careersembed.modules.jobpostings.api.response;

import com.example.careersembed.modules.jobpostings.domain.JobPosting;
import com.example.careersembed.shared.web.DateTimes;

public record JobPostingResponse(
    Long id,
    String title,
    String location,
    String jobType,
    Integer salaryFrom,
    Integer salaryTo,
    String description,
    boolean isActive,
    String slug,
    String publishedAt,
    String expiresAt,
    PostingWebsite website,
    PostingCompany company) {

  public record PostingWebsite(Long id, String name, String domain) {}

  public record PostingCompany(Long id, String name, String slug) {}

  public static JobPostingResponse from(JobPosting posting) {
    var website = posting.getWebsite();
    var company = website.getCompany();
    return new JobPostingResponse(
        posting.getId(),
        posting.getTitle(),
        posting.getLocation(),
        posting.getJobType().value(),
        posting.getSalaryFrom(),
        posting.getSalaryTo(),
        posting.getDescription(),
        posting.isActive(),
        posting.getSlug(),
        DateTimes.format(posting.getPublishedAt()),
        DateTimes.format(posting.getExpiresAt()),
        new PostingWebsite(website.getId(), website.getName(), website.getDomain()),
        new PostingCompany(company.getId(), company.getName(), company.getSlug()));
  }
}
