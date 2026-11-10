package com.example.careersembed.modules.websites.api.response;

import com.example.careersembed.modules.websites.domain.Website;
import com.example.careersembed.shared.web.DateTimes;

public record WebsiteDetailsResponse(
    Long id,
    String name,
    String domain,
    String slug,
    String logoUrl,
    String brandingColor,
    String fontFamily,
    String theme,
    WebsiteCompany company,
    long jobPostingsCount,
    String createdAt) {

  public record WebsiteCompany(Long id, String name, String slug) {}

  public static WebsiteDetailsResponse from(Website website, long jobPostingsCount) {
    return new WebsiteDetailsResponse(
        website.getId(),
        website.getName(),
        website.getDomain(),
        website.getSlug(),
        null,
        website.getBrandingColor(),
        website.getFontFamily(),
        website.getTheme().value(),
        new WebsiteCompany(
            website.getCompany().getId(),
            website.getCompany().getName(),
            website.getCompany().getSlug()),
        jobPostingsCount,
        DateTimes.format(website.getCreatedAt()));
  }
}
