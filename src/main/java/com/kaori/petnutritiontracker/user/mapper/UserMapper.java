package com.kaori.petnutritiontracker.user.mapper;

import com.kaori.petnutritiontracker.user.User;
import com.kaori.petnutritiontracker.user.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCreatedAt()
        );
    }

}