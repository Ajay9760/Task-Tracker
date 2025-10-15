package com.Assignment.Task_Tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String status;
    private String priority;
    private String projectId;
    private String projectName;
    private String assignedToId;
    private String assignedToUsername;
    private String createdById;
    private String createdByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}