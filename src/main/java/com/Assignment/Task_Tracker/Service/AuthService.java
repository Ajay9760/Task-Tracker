package com.Assignment.Task_Tracker.Service;

import com.Assignment.Task_Tracker.DTO.AuthResponse;
import com.Assignment.Task_Tracker.DTO.LoginRequest;
import com.Assignment.Task_Tracker.DTO.RegisterRequest;
import com.Assignment.Task_Tracker.DTO.UserResponse;
import com.Assignment.Task_Tracker.Entity.User;
import com.Assignment.Task_Tracker.Exception.BadRequestException;
import com.Assignment.Task_Tracker.Repository.UserRepository;
import com.Assignment.Task_Tracker.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getId());

        return new AuthResponse(
                "User registered successfully",
                mapToUserResponse(user),
                token
        );
    }

    public AuthResponse login(LoginRequest request) {
        log.info("User login attempt: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId());

        return new AuthResponse(
                "Login successful",
                mapToUserResponse(user),
                token
        );
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt()
        );
    }
}