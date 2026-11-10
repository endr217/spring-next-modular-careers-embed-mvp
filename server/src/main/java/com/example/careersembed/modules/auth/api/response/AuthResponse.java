package com.example.careersembed.modules.auth.api.response;

import com.example.careersembed.modules.users.api.response.UserResponse;

public record AuthResponse(UserResponse user, String token) {}
