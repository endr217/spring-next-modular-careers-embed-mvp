package com.example.careersembed.modules.companies.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OnboardingRequest(
    @NotBlank(message = "{validation.company_name.required}")
        @Size(max = 255, message = "{validation.max_255}")
        String companyName,
    @NotBlank(message = "{validation.company_slug.required}")
        @Size(max = 255, message = "{validation.max_255}")
        @Pattern(regexp = "^[a-z0-9-]+$", message = "{validation.company_slug.pattern}")
        String companySlug,
    @NotBlank(message = "{validation.company_email.required}")
        @Email(message = "{validation.email.invalid}")
        String companyEmail) {}
