package com.kaori.petnutritiontracker.auth;

import com.kaori.petnutritiontracker.user.dto.UserResponse;

public record AuthResponse(
        String accessToken,
        String tokenType,
        UserResponse user
) {
}