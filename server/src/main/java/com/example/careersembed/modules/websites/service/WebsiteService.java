package com.example.careersembed.modules.websites.service;

import com.example.careersembed.modules.companies.domain.Company;
import com.example.careersembed.modules.companies.infrastructure.repository.CompanyRepository;
import com.example.careersembed.modules.companies.service.CompanyAccess;
import com.example.careersembed.modules.websites.api.exception.WebsiteAccessException;
import com.example.careersembed.modules.websites.api.request.RegisterWebsiteRequest;
import com.example.careersembed.modules.websites.api.response.WebsiteDetailsResponse;
import com.example.careersembed.modules.websites.api.response.WebsiteResponse;
import com.example.careersembed.modules.websites.domain.Website;
import com.example.careersembed.modules.websites.domain.WebsiteTheme;
import com.example.careersembed.modules.websites.infrastructure.repository.WebsiteRepository;
import com.example.careersembed.shared.exception.FieldValidationException;
import com.example.careersembed.shared.slug.Slugs;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WebsiteService {

  private final WebsiteRepository websiteRepository;
  private final CompanyRepository companyRepository;
  private final CompanyAccess companyAccess;

  public WebsiteService(
      WebsiteRepository websiteRepository,
      CompanyRepository companyRepository,
      CompanyAccess companyAccess) {
    this.websiteRepository = websiteRepository;
    this.companyRepository = companyRepository;
    this.companyAccess = companyAccess;
  }

  @Transactional
  public WebsiteResponse register(long userId, RegisterWebsiteRequest request) {
    Company company =
        companyRepository
            .findById(request.companyId())
            .orElseThrow(
                () -> new FieldValidationException("company_id", "validation.company_id.unknown"));
    if (!companyAccess.isMember(userId, company.getId())) {
      throw WebsiteAccessException.userNotAuthorized();
    }
    String domain = request.domain().trim();
    if (websiteRepository.existsByDomain(domain)) {
      throw WebsiteAccessException.domainAlreadyExists();
    }
    Website website =
        websiteRepository.save(
            new Website(
                company,
                request.name().trim(),
                domain,
                uniqueSlug(request.name(), userId),
                blankToNull(request.brandingColor()),
                blankToNull(request.fontFamily()),
                WebsiteTheme.fromValue(request.theme())));
    return WebsiteResponse.from(website);
  }

  @Transactional(readOnly = true)
  public List<WebsiteResponse> listByCompany(long userId, long companyId) {
    if (!companyAccess.isMember(userId, companyId)) {
      throw WebsiteAccessException.userNotAuthorized();
    }
    return websiteRepository.findByCompanyIdOrderByIdAsc(companyId).stream()
        .map(WebsiteResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public WebsiteDetailsResponse details(long userId, long websiteId) {
    Website website = requireOwned(userId, websiteId);
    return WebsiteDetailsResponse.from(website, websiteRepository.countJobPostings(websiteId));
  }

  /** Loads a website the user may manage, or fails with the website error responses. */
  @Transactional(readOnly = true)
  public Website requireOwned(long userId, long websiteId) {
    Website website =
        websiteRepository
            .findWithCompanyById(websiteId)
            .orElseThrow(WebsiteAccessException::websiteNotFound);
    if (!companyAccess.isMember(userId, website.getCompany().getId())) {
      throw WebsiteAccessException.userNotAuthorized();
    }
    return website;
  }

  /** The slug comes from the name; on a clash the user id and then a counter are appended. */
  private String uniqueSlug(String name, long userId) {
    String slug = Slugs.of(name);
    if (!websiteRepository.existsBySlug(slug)) {
      return slug;
    }
    return Slugs.unique(slug + userId, websiteRepository::existsBySlug);
  }

  private static String blankToNull(String value) {
    return value == null || value.isBlank() ? null : value.trim();
  }
}
