package com.example.careersembed.modules.jobpostings.api.request;

import com.example.careersembed.modules.jobpostings.domain.JobType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Body for creating (website_id required) and updating (is_active required) a job posting. The
 * service checks those two operation-specific fields.
 */
public record JobPostingRequest(
    Long websiteId,
    @NotBlank(message = "{validation.title.required}")
        @Size(max = 255, message = "{validation.max_255}")
        String title,
    @NotBlank(message = "{validation.location.required}")
        @Size(max = 255, message = "{validation.max_255}")
        String location,
    @NotNull(message = "{validation.job_type.required}")
        @Pattern(regexp = JobType.PATTERN, message = "{validation.job_type.invalid}")
        String jobType,
    @Min(value = 0, message = "{validation.salary.min}") Integer salaryFrom,
    @Min(value = 0, message = "{validation.salary.min}") Integer salaryTo,
    @NotBlank(message = "{validation.description.required}") String description,
    String expiresAt,
    Boolean isActive) {}
