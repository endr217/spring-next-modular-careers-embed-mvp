package com.example.careersembed.modules.companies.service;

import com.example.careersembed.modules.companies.infrastructure.repository.CompanyMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Answers whether a user belongs to a company; other modules use it for authorization. */
@Service
public class CompanyAccess {

  private final CompanyMemberRepository memberRepository;

  public CompanyAccess(CompanyMemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Transactional(readOnly = true)
  public boolean isMember(long userId, long companyId) {
    return memberRepository.existsByUserIdAndCompanyId(userId, companyId);
  }
}
