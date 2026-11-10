package com.example.careersembed.modules.jobpostings.service;

import com.example.careersembed.modules.companies.service.CompanyAccess;
import com.example.careersembed.modules.jobpostings.api.request.JobPostingRequest;
import com.example.careersembed.modules.jobpostings.api.response.JobPostingResponse;
import com.example.careersembed.modules.jobpostings.domain.JobPosting;
import com.example.careersembed.modules.jobpostings.domain.JobType;
import com.example.careersembed.modules.jobpostings.infrastructure.repository.JobPostingRepository;
import com.example.careersembed.modules.websites.domain.Website;
import com.example.careersembed.modules.websites.service.WebsiteService;
import com.example.careersembed.shared.exception.FieldValidationException;
import com.example.careersembed.shared.exception.ForbiddenException;
import com.example.careersembed.shared.exception.ResourceNotFoundException;
import com.example.careersembed.shared.slug.Slugs;
import com.example.careersembed.shared.web.DateInput;
import com.example.careersembed.shared.web.PageRequests;
import com.example.careersembed.shared.web.PageResponse;
import java.time.Clock;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobPostingService {

  private final JobPostingRepository repository;
  private final WebsiteService websiteService;
  private final CompanyAccess companyAccess;
  private final Clock clock;

  public JobPostingService(
      JobPostingRepository repository,
      WebsiteService websiteService,
      CompanyAccess companyAccess,
      Clock clock) {
    this.repository = repository;
    this.websiteService = websiteService;
    this.companyAccess = companyAccess;
    this.clock = clock;
  }

  @Transactional
  public JobPostingResponse create(long userId, JobPostingRequest request) {
    if (request.websiteId() == null) {
      throw new FieldValidationException("website_id", "validation.website_id.required");
    }
    Website website = websiteService.requireOwned(userId, request.websiteId());
    String slug = Slugs.unique(Slugs.of(request.title()), repository::existsBySlug);
    JobPosting posting = new JobPosting(website, slug, clock.instant());
    apply(posting, request, true);
    return JobPostingResponse.from(repository.save(posting));
  }

  @Transactional
  public JobPostingResponse update(long userId, long id, JobPostingRequest request) {
    if (request.isActive() == null) {
      throw new FieldValidationException("is_active", "validation.is_active.required");
    }
    JobPosting posting = requireOwned(userId, id);
    apply(posting, request, request.isActive());
    return JobPostingResponse.from(posting);
  }

  @Transactional(readOnly = true)
  public JobPostingResponse get(long userId, long id) {
    return JobPostingResponse.from(requireOwned(userId, id));
  }

  @Transactional(readOnly = true)
  public PageResponse<JobPostingResponse> listForWebsite(long userId, long websiteId, int page) {
    websiteService.requireOwned(userId, websiteId);
    return PageResponse.of(
        repository.findByWebsiteId(websiteId, PageRequests.of(page, Sort.by("id"))),
        JobPostingResponse::from);
  }

  @Transactional
  public boolean delete(long userId, long id) {
    repository.delete(requireOwned(userId, id));
    return true;
  }

  /** Loads a posting of one of the user's companies. */
  @Transactional(readOnly = true)
  public JobPosting requireOwned(long userId, long id) {
    JobPosting posting =
        repository
            .findWithWebsiteById(id)
            .orElseThrow(() -> new ResourceNotFoundException("job_posting.not_found"));
    if (!companyAccess.isMember(userId, posting.getWebsite().getCompany().getId())) {
      throw new ForbiddenException();
    }
    return posting;
  }

  private static void apply(JobPosting posting, JobPostingRequest request, boolean active) {
    if (request.salaryFrom() != null
        && request.salaryTo() != null
        && request.salaryTo() < request.salaryFrom()) {
      throw new FieldValidationException("salary_to", "validation.salary.range");
    }
    posting.update(
        request.title().trim(),
        request.location().trim(),
        JobType.fromValue(request.jobType()),
        request.salaryFrom(),
        request.salaryTo(),
        request.description(),
        DateInput.parse("expires_at", request.expiresAt()),
        active);
  }
}
