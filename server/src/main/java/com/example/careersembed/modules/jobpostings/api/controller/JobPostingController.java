package com.example.careersembed.modules.jobpostings.api.controller;

import com.example.careersembed.modules.jobpostings.api.request.JobPostingRequest;
import com.example.careersembed.modules.jobpostings.api.response.JobPostingResponse;
import com.example.careersembed.modules.jobpostings.service.JobPostingService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobPostingController {

  private final JobPostingService service;
  private final CurrentUser currentUser;

  public JobPostingController(JobPostingService service, CurrentUser currentUser) {
    this.service = service;
    this.currentUser = currentUser;
  }

  @GetMapping("/websites/{websiteId:[0-9]+}/job-postings")
  public PageResponse<JobPostingResponse> index(
      @PathVariable long websiteId, @RequestParam(defaultValue = "1") int page) {
    return service.listForWebsite(currentUser.id(), websiteId, page);
  }

  @PostMapping("/job-postings")
  @ResponseStatus(HttpStatus.CREATED)
  public JobPostingResponse store(@Valid @RequestBody JobPostingRequest request) {
    return service.create(currentUser.id(), request);
  }

  @GetMapping("/job-postings/{id:[0-9]+}")
  public JobPostingResponse show(@PathVariable long id) {
    return service.get(currentUser.id(), id);
  }

  @RequestMapping(
      path = "/job-postings/{id:[0-9]+}",
      method = {RequestMethod.PUT, RequestMethod.PATCH})
  public JobPostingResponse update(
      @PathVariable long id, @Valid @RequestBody JobPostingRequest request) {
    return service.update(currentUser.id(), id, request);
  }

  @DeleteMapping("/job-postings/{id:[0-9]+}")
  public Map<String, Boolean> destroy(@PathVariable long id) {
    return Map.of("success", service.delete(currentUser.id(), id));
  }
}
