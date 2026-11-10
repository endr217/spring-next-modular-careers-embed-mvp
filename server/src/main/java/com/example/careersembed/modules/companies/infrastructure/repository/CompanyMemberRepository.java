package com.example.careersembed.modules.companies.infrastructure.repository;

import com.example.careersembed.modules.companies.domain.CompanyMember;
import com.example.careersembed.shared.repository.BaseRepository;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;

public interface CompanyMemberRepository extends BaseRepository<CompanyMember, Long> {

  @EntityGraph(attributePaths = "company")
  List<CompanyMember> findByUserIdOrderByIdAsc(Long userId);

  boolean existsByUserIdAndCompanyId(Long userId, Long companyId);
}
