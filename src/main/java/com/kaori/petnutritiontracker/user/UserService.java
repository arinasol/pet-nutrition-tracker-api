package com.kaori.petnutritiontracker.user;

import com.kaori.petnutritiontracker.auth.AuthResponse;
import com.kaori.petnutritiontracker.auth.JwtService;
import com.kaori.petnutritiontracker.user.dto.LoginRequest;
import com.kaori.petnutritiontracker.user.dto.RegisterRequest;
import com.kaori.petnutritiontracker.user.dto.UserResponse;
import com.kaori.petnutritiontracker.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered"
            );
        }

        User savedUser = userRepository.save(createUser(request));

        return userMapper.toResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));


        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        String accessToken = jwtService.generateToken(user.getEmail());

        return new AuthResponse(
                accessToken,
                "Bearer",
                userMapper.toResponse(user)
        );
    }

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        return userMapper.toResponse(user);
    }

    private User createUser(RegisterRequest request) {

        User user = new User();

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setCreatedAt(LocalDateTime.now());

        return user;
    }
}