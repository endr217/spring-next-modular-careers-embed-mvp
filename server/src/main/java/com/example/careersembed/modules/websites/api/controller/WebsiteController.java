package com.example.careersembed.modules.websites.api.controller;

import com.example.careersembed.modules.websites.api.request.RegisterWebsiteRequest;
import com.example.careersembed.modules.websites.api.response.WebsiteDetailsResponse;
import com.example.careersembed.modules.websites.api.response.WebsiteResponse;
import com.example.careersembed.modules.websites.service.WebsiteService;
import com.example.careersembed.security.CurrentUser;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/websites")
public class WebsiteController {

  private final WebsiteService websiteService;
  private final CurrentUser currentUser;

  public WebsiteController(WebsiteService websiteService, CurrentUser currentUser) {
    this.websiteService = websiteService;
    this.currentUser = currentUser;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public WebsiteResponse register(@Valid @RequestBody RegisterWebsiteRequest request) {
    return websiteService.register(currentUser.id(), request);
  }

  @GetMapping("/company/{companyId:[0-9]+}")
  public List<WebsiteResponse> listByCompany(@PathVariable long companyId) {
    return websiteService.listByCompany(currentUser.id(), companyId);
  }

  @GetMapping("/{websiteId:[0-9]+}")
  public WebsiteDetailsResponse show(@PathVariable long websiteId) {
    return websiteService.details(currentUser.id(), websiteId);
  }
}
