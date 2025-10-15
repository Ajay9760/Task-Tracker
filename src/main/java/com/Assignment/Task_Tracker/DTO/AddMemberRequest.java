package com.Assignment.Task_Tracker.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddMemberRequest {
    @NotBlank(message = "User ID is required")
    private String userId;
}