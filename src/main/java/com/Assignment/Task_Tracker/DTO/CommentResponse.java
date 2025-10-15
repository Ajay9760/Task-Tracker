package com.Assignment.Task_Tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponse {
    private String id;
    private String content;
    private String userId;
    private String username;
    private LocalDateTime createdAt;
}