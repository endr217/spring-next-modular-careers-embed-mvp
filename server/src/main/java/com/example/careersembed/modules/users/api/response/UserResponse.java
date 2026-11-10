package com.example.careersembed.modules.users.api.response;

import com.example.careersembed.modules.users.domain.User;
import com.example.careersembed.shared.web.DateTimes;

public record UserResponse(Long id, String name, String surname, String email, String createdAt) {

  public static UserResponse from(User user) {
    return new UserResponse(
        user.getId(),
        user.getName(),
        user.getSurname(),
        user.getEmail(),
        DateTimes.iso(user.getCreatedAt()));
  }
}
