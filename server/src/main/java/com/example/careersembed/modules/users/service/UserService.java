package com.example.careersembed.modules.users.service;

import com.example.careersembed.modules.users.domain.User;
import com.example.careersembed.modules.users.infrastructure.repository.UserRepository;
import com.example.careersembed.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public User get(long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));
  }
}
