package com.example.careersembed.modules.embed.service;

import com.example.careersembed.modules.embed.api.response.EmbedCodeResponse;
import com.example.careersembed.modules.embed.api.response.EmbedWebsiteResponse;
import com.example.careersembed.modules.embed.domain.LoadStrategy;
import com.example.careersembed.modules.embed.domain.ScriptTag;
import com.example.careersembed.modules.jobpostings.infrastructure.repository.JobPostingRepository;
import com.example.careersembed.modules.websites.domain.Website;
import com.example.careersembed.modules.websites.infrastructure.repository.WebsiteRepository;
import com.example.careersembed.shared.exception.FieldValidationException;
import java.time.Clock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmbedService {

  private final WebsiteRepository websiteRepository;
  private final JobPostingRepository jobPostingRepository;
  private final EmbedRenderer renderer;
  private final EmbedUrls urls;
  private final Clock clock;

  public EmbedService(
      WebsiteRepository websiteRepository,
      JobPostingRepository jobPostingRepository,
      EmbedRenderer renderer,
      EmbedUrls urls,
      Clock clock) {
    this.websiteRepository = websiteRepository;
    this.jobPostingRepository = jobPostingRepository;
    this.renderer = renderer;
    this.urls = urls;
    this.clock = clock;
  }

  @Transactional(readOnly = true)
  public EmbedCodeResponse embedCode(String slug, String loadStrategy) {
    Website website = findWebsite(slug);
    LoadStrategy strategy =
        loadStrategy == null || loadStrategy.isBlank()
            ? LoadStrategy.DEFER
            : LoadStrategy.fromValue(loadStrategy)
                .orElseThrow(
                    () ->
                        new FieldValidationException(
                            "loadStrategy", "validation.load_strategy.invalid"));
    String code = new ScriptTag(urls.script(website.getSlug()), website.getSlug(), strategy).toHtml();
    return new EmbedCodeResponse(code, EmbedWebsiteResponse.from(website));
  }

  @Transactional(readOnly = true)
  public String script(String slug) {
    return renderer.script(findWebsite(slug));
  }

  @Transactional(readOnly = true)
  public String view(String slug) {
    Website website = findWebsite(slug);
    return renderer.view(
        website, jobPostingRepository.findVisible(website.getId(), clock.instant()));
  }

  private Website findWebsite(String slug) {
    return websiteRepository
        .findBySlug(slug)
        .orElseThrow(
            () -> new FieldValidationException("websiteSlug", "validation.website_slug.unknown"));
  }
}
