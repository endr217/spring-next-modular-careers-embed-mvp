package com.example.careersembed.modules.applications.infrastructure.repository;

import com.example.careersembed.modules.applications.domain.Application;
import com.example.careersembed.shared.repository.BaseRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface ApplicationRepository extends BaseRepository<Application, Long> {

  @EntityGraph(attributePaths = {"jobPosting", "jobPosting.website", "jobPosting.website.company"})
  Optional<Application> findWithJobPostingById(Long id);

  @EntityGraph(attributePaths = {"jobPosting", "jobPosting.website", "jobPosting.website.company"})
  Page<Application> findByJobPostingId(Long jobPostingId, Pageable pageable);
}
