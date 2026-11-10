package com.example.careersembed.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/** Resolves the authenticated user id from the JWT subject. */
@Component
public class CurrentUser {

  public long id() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof Jwt jwt) {
      return Long.parseLong(jwt.getSubject());
    }
    throw new IllegalStateException("No authenticated user");
  }
}
