package com.example.careersembed.modules.companies.service;

import com.example.careersembed.modules.companies.api.response.ProfileResponse;
import com.example.careersembed.modules.companies.infrastructure.repository.CompanyMemberRepository;
import com.example.careersembed.modules.users.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

  private final UserService userService;
  private final CompanyMemberRepository memberRepository;

  public ProfileService(UserService userService, CompanyMemberRepository memberRepository) {
    this.userService = userService;
    this.memberRepository = memberRepository;
  }

  @Transactional(readOnly = true)
  public ProfileResponse get(long userId) {
    return ProfileResponse.of(
        userService.get(userId), memberRepository.findByUserIdOrderByIdAsc(userId));
  }
}
