package com.Assignment.Task_Tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class TeamResponse {
    private String id;
    private String name;
    private String description;
    private String createdById;
    private String createdByUsername;
    private LocalDateTime createdAt;
    private Set<String> memberIds;
}