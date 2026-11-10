package com.example.careersembed.modules.companies.service;

import com.example.careersembed.modules.companies.api.exception.SlugTakenException;
import com.example.careersembed.modules.companies.api.request.OnboardingRequest;
import com.example.careersembed.modules.companies.api.response.OnboardingResponse;
import com.example.careersembed.modules.companies.domain.Company;
import com.example.careersembed.modules.companies.domain.CompanyMember;
import com.example.careersembed.modules.companies.domain.CompanyRole;
import com.example.careersembed.modules.companies.infrastructure.repository.CompanyMemberRepository;
import com.example.careersembed.modules.companies.infrastructure.repository.CompanyRepository;
import com.example.careersembed.modules.users.domain.User;
import com.example.careersembed.modules.users.service.UserService;
import com.example.careersembed.shared.exception.FieldValidationException;
import com.example.careersembed.shared.mail.MailMessage;
import com.example.careersembed.shared.mail.MailService;
import com.example.careersembed.shared.slug.Slugs;
import com.example.careersembed.shared.web.Html;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OnboardingService {

  private final CompanyRepository companyRepository;
  private final CompanyMemberRepository memberRepository;
  private final UserService userService;
  private final MailService mailService;
  private final MessageSource messages;

  public OnboardingService(
      CompanyRepository companyRepository,
      CompanyMemberRepository memberRepository,
      UserService userService,
      MailService mailService,
      MessageSource messages) {
    this.companyRepository = companyRepository;
    this.memberRepository = memberRepository;
    this.userService = userService;
    this.mailService = mailService;
    this.messages = messages;
  }

  @Transactional
  public OnboardingResponse complete(long userId, OnboardingRequest request) {
    String email = request.companyEmail().trim();
    if (companyRepository.existsByEmail(email)) {
      throw new FieldValidationException("company_email", "validation.company_email.taken");
    }
    String requested = request.companySlug();
    String slug = Slugs.unique(Slugs.of(requested), companyRepository::existsBySlug);
    if (!slug.equals(requested)) {
      throw new SlugTakenException(requested, slug);
    }

    User user = userService.get(userId);
    Company company = companyRepository.save(new Company(request.companyName().trim(), slug, email));
    memberRepository.save(new CompanyMember(userId, company, CompanyRole.OWNER));
    mailService.send(welcomeMail(user));

    return OnboardingResponse.of(
        msg("company.registration_completed"), msg("company.onboarding_success"), user, company);
  }

  private MailMessage welcomeMail(User user) {
    String title = msg("mail.onboarding.subject");
    String welcome = msg("mail.onboarding.welcome", user.getName());
    String complete = msg("mail.onboarding.complete");
    String thanks = msg("mail.onboarding.thanks");
    String html =
        "<h1>"
            + Html.escape(welcome)
            + "</h1><p>"
            + Html.escape(complete)
            + "</p><p>"
            + Html.escape(thanks)
            + "</p>";
    return new MailMessage(
        user.getEmail(),
        user.getName(),
        title,
        String.join("\n\n", welcome, complete, thanks),
        html);
  }

  private String msg(String key, Object... args) {
    return messages.getMessage(key, args, LocaleContextHolder.getLocale());
  }
}
