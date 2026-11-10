package com.example.careersembed.modules.applications.api.controller;

import com.example.careersembed.modules.applications.api.request.ApplicationRequest;
import com.example.careersembed.modules.applications.api.response.ApplicationResponse;
import com.example.careersembed.modules.applications.service.ApplicationService;
import com.example.careersembed.security.CurrentUser;
import com.example.careersembed.shared.web.PageResponse;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

  private final ApplicationService service;
  private final CurrentUser currentUser;

  public ApplicationController(ApplicationService service, CurrentUser currentUser) {
    this.service = service;
    this.currentUser = currentUser;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApplicationResponse store(@Valid @RequestBody ApplicationRequest request) {
    return service.createForCompany(currentUser.id(), request);
  }

  @GetMapping("/{id:[0-9]+}")
  public ApplicationResponse show(@PathVariable long id) {
    return service.get(currentUser.id(), id);
  }

  @GetMapping("/job/{jobPostingId:[0-9]+}")
  public PageResponse<ApplicationResponse> index(
      @PathVariable long jobPostingId, @RequestParam(defaultValue = "1") int page) {
    return service.listForJob(currentUser.id(), jobPostingId, page);
  }

  @DeleteMapping("/{id:[0-9]+}")
  public Map<String, Boolean> destroy(@PathVariable long id) {
    return Map.of("success", service.delete(currentUser.id(), id));
  }
}
