package com.example.careersembed.modules.embed.api.controller;

import com.example.careersembed.modules.applications.api.request.ApplicationRequest;
import com.example.careersembed.modules.applications.api.response.ApplicationResponse;
import com.example.careersembed.modules.applications.service.ApplicationService;
import com.example.careersembed.modules.embed.api.response.EmbedCodeResponse;
import com.example.careersembed.modules.embed.service.EmbedService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Public endpoints used by the widget on customer websites. */
@RestController
public class EmbedController {

  private static final String JAVASCRIPT = "application/javascript;charset=UTF-8";

  private final EmbedService embedService;
  private final ApplicationService applicationService;

  public EmbedController(EmbedService embedService, ApplicationService applicationService) {
    this.embedService = embedService;
    this.applicationService = applicationService;
  }

  @GetMapping("/websites/{websiteSlug}/embed-code")
  public EmbedCodeResponse embedCode(
      @PathVariable String websiteSlug, @RequestParam(required = false) String loadStrategy) {
    return embedService.embedCode(websiteSlug, loadStrategy);
  }

  @GetMapping(value = "/embeds/{websiteSlug}/script.js", produces = JAVASCRIPT)
  public String script(@PathVariable String websiteSlug) {
    return embedService.script(websiteSlug);
  }

  @GetMapping(value = "/embeds/{websiteSlug}/view", produces = MediaType.TEXT_HTML_VALUE)
  public String view(@PathVariable String websiteSlug) {
    return embedService.view(websiteSlug);
  }

  @PostMapping("/embeds/applications")
  @ResponseStatus(HttpStatus.CREATED)
  public ApplicationResponse apply(@Valid @RequestBody ApplicationRequest request) {
    return applicationService.submitFromEmbed(request);
  }
}
