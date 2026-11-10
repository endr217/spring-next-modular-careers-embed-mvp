package com.example.careersembed.modules.auth.api.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

public record ResetPasswordRequest(
    @NotBlank(message = "{validation.token.required}") String token,
    @NotBlank(message = "{validation.email.required}") @Email(message = "{validation.email.invalid}")
        String email,
    @NotBlank(message = "{validation.password.required}")
        @Size(min = 8, max = 255, message = "{validation.password.min}")
        String password,
    @NotBlank(message = "{validation.password_confirmation.required}")
        String passwordConfirmation) {

  @AssertTrue(message = "{validation.password.confirmed}")
  public boolean isPassword() {
    return password == null || Objects.equals(password, passwordConfirmation);
  }
}
