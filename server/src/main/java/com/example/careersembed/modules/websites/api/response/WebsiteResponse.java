package com.example.careersembed.modules.websites.api.response;

import com.example.careersembed.modules.websites.domain.Website;
import com.example.careersembed.shared.web.DateTimes;

public record WebsiteResponse(
    Long id,
    String name,
    String domain,
    String slug,
    String logoUrl,
    String brandingColor,
    String fontFamily,
    String theme,
    String createdAt) {

  public static WebsiteResponse from(Website website) {
    return new WebsiteResponse(
        website.getId(),
        website.getName(),
        website.getDomain(),
        website.getSlug(),
        null,
        website.getBrandingColor(),
        website.getFontFamily(),
        website.getTheme().value(),
        DateTimes.format(website.getCreatedAt()));
  }
}
