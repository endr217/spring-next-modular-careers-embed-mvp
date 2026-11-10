package com.example.careersembed.modules.companies.api.response;

import com.example.careersembed.modules.companies.domain.Company;
import com.example.careersembed.modules.users.domain.User;
import com.example.careersembed.shared.web.DateTimes;

/** Keeps the nested {message, data: {message, data: {user, company}}} shape of the API. */
public record OnboardingResponse(String message, Result data) {

  public record Result(String message, Payload data) {}

  public record Payload(UserPayload user, CompanyPayload company) {}

  public record UserPayload(
      Long id,
      String name,
      String surname,
      String email,
      String emailVerifiedAt,
      short status,
      String createdAt,
      String updatedAt) {

    static UserPayload from(User user) {
      return new UserPayload(
          user.getId(),
          user.getName(),
          user.getSurname(),
          user.getEmail(),
          DateTimes.iso(user.getEmailVerifiedAt()),
          user.getStatus(),
          DateTimes.iso(user.getCreatedAt()),
          DateTimes.iso(user.getUpdatedAt()));
    }
  }

  public record CompanyPayload(
      Long id, String name, String slug, String email, String createdAt, String updatedAt) {

    static CompanyPayload from(Company company) {
      return new CompanyPayload(
          company.getId(),
          company.getName(),
          company.getSlug(),
          company.getEmail(),
          DateTimes.iso(company.getCreatedAt()),
          DateTimes.iso(company.getUpdatedAt()));
    }
  }

  public static OnboardingResponse of(
      String message, String resultMessage, User user, Company company) {
    return new OnboardingResponse(
        message,
        new Result(
            resultMessage, new Payload(UserPayload.from(user), CompanyPayload.from(company))));
  }
}
