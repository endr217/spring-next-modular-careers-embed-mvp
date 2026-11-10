package com.example.careersembed.modules.applications.service;

import com.example.careersembed.modules.applications.api.request.ApplicationRequest;
import com.example.careersembed.modules.applications.api.response.ApplicationResponse;
import com.example.careersembed.modules.applications.domain.Application;
import com.example.careersembed.modules.applications.infrastructure.repository.ApplicationRepository;
import com.example.careersembed.modules.companies.service.CompanyAccess;
import com.example.careersembed.modules.jobpostings.domain.JobPosting;
import com.example.careersembed.modules.jobpostings.infrastructure.repository.JobPostingRepository;
import com.example.careersembed.modules.jobpostings.service.JobPostingService;
import com.example.careersembed.shared.exception.FieldValidationException;
import com.example.careersembed.shared.exception.ForbiddenException;
import com.example.careersembed.shared.exception.ResourceNotFoundException;
import com.example.careersembed.shared.web.PageRequests;
import com.example.careersembed.shared.web.PageResponse;
import java.time.Clock;
import java.time.Instant;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationService {

  private final ApplicationRepository repository;
  private final JobPostingRepository jobPostingRepository;
  private final JobPostingService jobPostingService;
  private final CompanyAccess companyAccess;
  private final ApplicationEventPublisher events;
  private final Clock clock;

  public ApplicationService(
      ApplicationRepository repository,
      JobPostingRepository jobPostingRepository,
      JobPostingService jobPostingService,
      CompanyAccess companyAccess,
      ApplicationEventPublisher events,
      Clock clock) {
    this.repository = repository;
    this.jobPostingRepository = jobPostingRepository;
    this.jobPostingService = jobPostingService;
    this.companyAccess = companyAccess;
    this.events = events;
    this.clock = clock;
  }

  /** Created from the dashboard by a member of the posting's company. */
  @Transactional
  public ApplicationResponse createForCompany(long userId, ApplicationRequest request) {
    JobPosting posting = findPosting(request.jobPostingId());
    if (!companyAccess.isMember(userId, posting.getWebsite().getCompany().getId())) {
      throw new ForbiddenException();
    }
    return store(posting, request);
  }

  /** Submitted by a candidate through the public embed; only open postings accept applications. */
  @Transactional
  public ApplicationResponse submitFromEmbed(ApplicationRequest request) {
    JobPosting posting = findPosting(request.jobPostingId());
    Instant now = clock.instant();
    boolean open =
        posting.isActive()
            && !posting.getPublishedAt().isAfter(now)
            && (posting.getExpiresAt() == null || posting.getExpiresAt().isAfter(now));
    if (!open) {
      throw new FieldValidationException("job_posting_id", "validation.job_posting_id.closed");
    }
    return store(posting, request);
  }

  @Transactional(readOnly = true)
  public ApplicationResponse get(long userId, long id) {
    return ApplicationResponse.from(requireOwned(userId, id));
  }

  @Transactional(readOnly = true)
  public PageResponse<ApplicationResponse> listForJob(long userId, long jobPostingId, int page) {
    jobPostingService.requireOwned(userId, jobPostingId);
    return PageResponse.of(
        repository.findByJobPostingId(
            jobPostingId, PageRequests.of(page, Sort.by(Sort.Direction.DESC, "submittedAt"))),
        ApplicationResponse::from);
  }

  @Transactional
  public boolean delete(long userId, long id) {
    repository.delete(requireOwned(userId, id));
    return true;
  }

  private ApplicationResponse store(JobPosting posting, ApplicationRequest request) {
    Application application =
        repository.save(
            new Application(
                posting,
                request.name().trim(),
                request.email().trim(),
                blankToNull(request.motivationText()),
                request.cvFilePath().trim(),
                clock.instant()));
    ApplicationResponse response = ApplicationResponse.from(application);
    events.publishEvent(new ApplicationSubmitted(response));
    return response;
  }

  private JobPosting findPosting(Long id) {
    return jobPostingRepository
        .findWithWebsiteById(id)
        .orElseThrow(
            () -> new FieldValidationException("job_posting_id", "validation.job_posting_id.unknown"));
  }

  private Application requireOwned(long userId, long id) {
    Application application =
        repository
            .findWithJobPostingById(id)
            .orElseThrow(() -> new ResourceNotFoundException("application.not_found"));
    long companyId = application.getJobPosting().getWebsite().getCompany().getId();
    if (!companyAccess.isMember(userId, companyId)) {
      throw new ForbiddenException();
    }
    return application;
  }

  private static String blankToNull(String value) {
    return value == null || value.isBlank() ? null : value.trim();
  }
}
