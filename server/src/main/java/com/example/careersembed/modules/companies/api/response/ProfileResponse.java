package com.example.careersembed.modules.companies.api.response;

import com.example.careersembed.modules.companies.domain.CompanyMember;
import com.example.careersembed.modules.users.domain.User;
import com.example.careersembed.shared.web.DateTimes;
import java.util.List;

public record ProfileResponse(
    ProfileUser user,
    boolean hasCompanies,
    int companiesCount,
    List<ProfileCompany> companies,
    ProfileCompany primaryCompany) {

  public record ProfileUser(Long id, String name, String email, String createdAt) {}

  public record ProfileCompany(
      Long id, String name, String slug, String logoUrl, String userRole, boolean isPrimary) {

    static ProfileCompany from(CompanyMember member) {
      return new ProfileCompany(
          member.getCompany().getId(),
          member.getCompany().getName(),
          member.getCompany().getSlug(),
          null,
          member.getRole().value(),
          false);
    }
  }

  public static ProfileResponse of(User user, List<CompanyMember> memberships) {
    List<ProfileCompany> companies = memberships.stream().map(ProfileCompany::from).toList();
    return new ProfileResponse(
        new ProfileUser(
            user.getId(), user.getName(), user.getEmail(), DateTimes.iso(user.getCreatedAt())),
        !companies.isEmpty(),
        companies.size(),
        companies,
        companies.isEmpty() ? null : companies.getFirst());
  }
}
