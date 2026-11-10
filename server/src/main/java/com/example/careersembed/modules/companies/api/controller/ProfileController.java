package com.example.careersembed.modules.companies.api.controller;

import com.example.careersembed.modules.companies.api.response.ProfileResponse;
import com.example.careersembed.modules.companies.service.ProfileService;
import com.example.careersembed.security.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The signed-in user together with the companies they belong to. */
@RestController
@RequestMapping("/auth")
public class ProfileController {

  private final ProfileService profileService;
  private final CurrentUser currentUser;

  public ProfileController(ProfileService profileService, CurrentUser currentUser) {
    this.profileService = profileService;
    this.currentUser = currentUser;
  }

  @GetMapping("/user")
  public ProfileResponse show() {
    return profileService.get(currentUser.id());
  }
}
