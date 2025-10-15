package com.Assignment.Task_Tracker.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private LocalDateTime dueDate;
    private String priority = "Medium";

    @NotNull(message = "Project ID is required")
    private String projectId;

    private String assignedToId;
}
