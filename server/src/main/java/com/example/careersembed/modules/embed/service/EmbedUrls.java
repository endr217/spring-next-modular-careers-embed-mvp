package com.example.careersembed.modules.embed.service;

import com.example.careersembed.config.AppProperties;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

/** Absolute URLs of the public embed endpoints. */
@Component
public class EmbedUrls {

  private final String base;

  public EmbedUrls(AppProperties properties) {
    this.base = properties.publicUrl().replaceAll("/+$", "") + "/api/embeds";
  }

  public String script(String slug) {
    return base + "/" + encode(slug) + "/script.js";
  }

  public String view(String slug) {
    return base + "/" + encode(slug) + "/view";
  }

  public String store() {
    return base + "/applications";
  }

  private static String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
