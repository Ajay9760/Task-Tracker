package com.Assignment.Task_Tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private UserResponse user;
    private String token;
}