package com.Assignment.Task_Tracker.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String status;
}