package com.example.careersembed.modules.embed.api.response;

import com.example.careersembed.modules.websites.domain.Website;

public record EmbedWebsiteResponse(
    String name, String slug, String brandingColor, String logoPath, String fontFamily) {

  public static EmbedWebsiteResponse from(Website website) {
    return new EmbedWebsiteResponse(
        website.getName(),
        website.getSlug(),
        website.getBrandingColor(),
        website.getLogoPath(),
        website.getFontFamily());
  }
}
