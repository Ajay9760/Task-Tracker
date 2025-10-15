package com.Assignment.Task_Tracker.DTO;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String username;
    private String firstName;
    private String lastName;
}
