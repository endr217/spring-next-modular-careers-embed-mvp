package com.example.careersembed.modules.websites.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterWebsiteRequest(
    @NotNull(message = "{validation.company_id.required}") Long companyId,
    @NotBlank(message = "{validation.name.required}") @Size(max = 255, message = "{validation.max_255}")
        String name,
    @NotBlank(message = "{validation.domain.required}")
        @Size(max = 255, message = "{validation.max_255}")
        String domain,
    @Size(max = 7, message = "{validation.branding_color.size}") String brandingColor,
    @Size(max = 255, message = "{validation.max_255}") String fontFamily,
    @Pattern(regexp = "^(light|dark)$", message = "{validation.theme.invalid}") String theme) {}
