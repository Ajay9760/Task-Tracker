package com.Assignment.Task_Tracker.DTO;

import com.Assignment.Task_Tracker.Entity.Attachment;
import com.Assignment.Task_Tracker.Entity.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for transferring task data between layers.
 * Includes all relevant task information including related entities.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse {
    @NotBlank(message = "Task ID is required")
    private String id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    @NotBlank(message = "Status is required")
    private String status;

    @NotBlank(message = "Priority is required")
    private String priority;

    // Team information
    @NotBlank(message = "Team ID is required")
    private String teamId;
    private String teamName;

    // Assigned user information
    private String assignedToId;
    private String assignedToName;
    private String assignedToEmail;

    // Creator information
    @NotBlank(message = "Creator ID is required")
    private String createdById;
    private String createdByName;
    private String createdByEmail;

    // Related entities
    private Set<CommentResponse> comments;
    private Set<AttachmentResponse> attachments;

    // Timestamps
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private LocalDateTime updatedAt;
    
    // Additional metadata
    private Integer commentCount;
    private Integer attachmentCount;
}