package com.Assignment.Task_Tracker.Entity;

import com.Assignment.Task_Tracker.DTO.TaskResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper class to convert between Task entity and TaskResponse DTO.
 */
@Component
public class TaskMapper {
    
    private TaskMapper() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Maps a Task entity to a TaskResponse DTO.
     *
     * @param task the task entity to map
     * @return the mapped TaskResponse DTO
     */
    public static TaskResponse mapToTaskResponse(Task task) {
        if (task == null) {
            return null;
        }
        
        try {
            return TaskResponse.builder()
                    .id(task.getId())
                    .title(task.getTitle() != null ? task.getTitle() : "")
                    .description(task.getDescription() != null ? task.getDescription() : "")
                    .status(task.getStatus() != null ? task.getStatus().name() : Task.TaskStatus.OPEN.name())
                    .priority(task.getPriority() != null ? task.getPriority().name() : Task.Priority.MEDIUM.name())
                    .dueDate(task.getDueDate())
                    .assignedToId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                    .assignedToName(task.getAssignedTo() != null ? 
                            (task.getAssignedTo().getFirstName() + " " + task.getAssignedTo().getLastName()).trim() : null)
                    .assignedToEmail(task.getAssignedTo() != null ? task.getAssignedTo().getEmail() : null)
                    .createdById(task.getCreatedBy() != null ? task.getCreatedBy().getId() : null)
                    .createdByName(task.getCreatedBy() != null ? 
                            (task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName()).trim() : null)
                    .createdByEmail(task.getCreatedBy() != null ? task.getCreatedBy().getEmail() : null)
                    .teamId(task.getTeam() != null ? task.getTeam().getId() : null)
                    .teamName(task.getTeam() != null ? task.getTeam().getName() : null)
                    .createdAt(task.getCreatedAt())
                    .updatedAt(task.getUpdatedAt())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error mapping Task to TaskResponse", e);
        }
    }
}