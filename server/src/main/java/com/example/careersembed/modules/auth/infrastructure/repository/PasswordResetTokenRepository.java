package com.example.careersembed.modules.auth.infrastructure.repository;

import com.example.careersembed.modules.auth.domain.PasswordResetToken;
import com.example.careersembed.shared.repository.BaseRepository;

public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetToken, String> {}
