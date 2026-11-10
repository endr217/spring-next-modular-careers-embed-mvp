package com.example.careersembed.modules.auth.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "{validation.name.required}") @Size(max = 255, message = "{validation.max_255}")
        String name,
    @NotBlank(message = "{validation.surname.required}")
        @Size(max = 255, message = "{validation.max_255}")
        String surname,
    @NotBlank(message = "{validation.email.required}")
        @Email(message = "{validation.email.invalid}")
        @Pattern(regexp = "^[^A-Z]*$", message = "{validation.email.lowercase}")
        @Size(max = 255, message = "{validation.max_255}")
        String email,
    @NotBlank(message = "{validation.password.required}")
        @Size(min = 8, max = 255, message = "{validation.password.min}")
        String password) {}
