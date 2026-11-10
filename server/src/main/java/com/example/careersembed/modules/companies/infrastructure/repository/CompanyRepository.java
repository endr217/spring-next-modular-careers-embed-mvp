package com.example.careersembed.modules.companies.infrastructure.repository;

import com.example.careersembed.modules.companies.domain.Company;
import com.example.careersembed.shared.repository.BaseRepository;

public interface CompanyRepository extends BaseRepository<Company, Long> {

  boolean existsBySlug(String slug);

  boolean existsByEmail(String email);
}
