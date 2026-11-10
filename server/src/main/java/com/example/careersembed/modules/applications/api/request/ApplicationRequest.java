package com.example.careersembed.modules.applications.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApplicationRequest(
    @NotNull(message = "{validation.job_posting_id.required}") Long jobPostingId,
    @NotBlank(message = "{validation.name.required}") @Size(max = 255, message = "{validation.max_255}")
        String name,
    @NotBlank(message = "{validation.email.required}")
        @Email(message = "{validation.email.invalid}")
        @Size(max = 255, message = "{validation.max_255}")
        String email,
    @Size(max = 10000, message = "{validation.motivation_text.size}") String motivationText,
    @NotBlank(message = "{validation.cv_file_path.required}")
        @Size(max = 255, message = "{validation.max_255}")
        String cvFilePath) {}
