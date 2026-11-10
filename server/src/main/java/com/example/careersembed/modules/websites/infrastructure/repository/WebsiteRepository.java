package com.example.careersembed.modules.websites.infrastructure.repository;

import com.example.careersembed.modules.websites.domain.Website;
import com.example.careersembed.shared.repository.BaseRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

public interface WebsiteRepository extends BaseRepository<Website, Long> {

  @EntityGraph(attributePaths = "company")
  Optional<Website> findWithCompanyById(Long id);

  @EntityGraph(attributePaths = "company")
  Optional<Website> findBySlug(String slug);

  List<Website> findByCompanyIdOrderByIdAsc(Long companyId);

  boolean existsBySlug(String slug);

  boolean existsByDomain(String domain);

  @Query(value = "select count(*) from job_postings where website_id = :websiteId", nativeQuery = true)
  long countJobPostings(Long websiteId);
}
