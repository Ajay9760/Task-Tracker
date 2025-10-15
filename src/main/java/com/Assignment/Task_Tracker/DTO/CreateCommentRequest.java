package com.Assignment.Task_Tracker.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCommentRequest {
    @NotBlank(message = "Content is required")
    private String content;
}
