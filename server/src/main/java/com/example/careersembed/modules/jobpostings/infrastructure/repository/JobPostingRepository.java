package com.example.careersembed.modules.jobpostings.infrastructure.repository;

import com.example.careersembed.modules.jobpostings.domain.JobPosting;
import com.example.careersembed.shared.repository.BaseRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

public interface JobPostingRepository extends BaseRepository<JobPosting, Long> {

  @EntityGraph(attributePaths = {"website", "website.company"})
  Optional<JobPosting> findWithWebsiteById(Long id);

  @EntityGraph(attributePaths = {"website", "website.company"})
  Page<JobPosting> findByWebsiteId(Long websiteId, Pageable pageable);

  boolean existsBySlug(String slug);

  /** Postings visible in the embed: active, already published and not expired. */
  @Query(
      """
      select j from JobPosting j
      where j.website.id = :websiteId
        and j.active = true
        and j.publishedAt <= :now
        and (j.expiresAt is null or j.expiresAt > :now)
      order by j.publishedAt desc
      """)
  List<JobPosting> findVisible(Long websiteId, Instant now);
}
