package com.example.careersembed.modules.companies.api.controller;

import com.example.careersembed.modules.companies.api.request.OnboardingRequest;
import com.example.careersembed.modules.companies.api.response.OnboardingResponse;
import com.example.careersembed.modules.companies.service.OnboardingService;
import com.example.careersembed.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/onboarding")
public class OnboardingController {

  private final OnboardingService onboardingService;
  private final CurrentUser currentUser;

  public OnboardingController(OnboardingService onboardingService, CurrentUser currentUser) {
    this.onboardingService = onboardingService;
    this.currentUser = currentUser;
  }

  @PostMapping("/company")
  @ResponseStatus(HttpStatus.CREATED)
  public OnboardingResponse complete(@Valid @RequestBody OnboardingRequest request) {
    return onboardingService.complete(currentUser.id(), request);
  }
}
