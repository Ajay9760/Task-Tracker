package com.Assignment.Task_Tracker.Service;

import com.Assignment.Task_Tracker.DTO.CreateTaskRequest;
import com.Assignment.Task_Tracker.DTO.TaskResponse;
import com.Assignment.Task_Tracker.DTO.UpdateTaskRequest;
import com.Assignment.Task_Tracker.Entity.Task;
import com.Assignment.Task_Tracker.Entity.Team;
import com.Assignment.Task_Tracker.Entity.User;
import com.Assignment.Task_Tracker.Exception.ResourceNotFoundException;
import com.Assignment.Task_Tracker.Repository.TaskRepository;
import com.Assignment.Task_Tracker.Repository.TeamRepository;
import com.Assignment.Task_Tracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public TaskResponse createTask(String userId, CreateTaskRequest request) {
        log.info("Creating new task for user: {}", userId);
        
        // Validate input
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        
        // Find and validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Find and validate team/project
        Team team = teamRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", request.getProjectId()));

        // Check if the user is a member of the team
        if (!team.getMembers().contains(user)) {
            throw new IllegalStateException(String.format("User %s is not authorized to create tasks in team %s", 
                user.getId(), team.getId()));
        }

        // Build task
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .priority(request.getPriority() != null ? 
                    Task.Priority.valueOf(request.getPriority().toUpperCase()) : 
                    Task.Priority.MEDIUM)
                .team(team)
                .createdBy(user)
                .status(Task.TaskStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        // Handle task assignment if specified
        if (request.getAssignedToId() != null && !request.getAssignedToId().trim().isEmpty()) {
            User assignedTo = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssignedToId()));
            
            if (!team.getMembers().contains(assignedTo)) {
                throw new IllegalStateException(String.format(
                    "Cannot assign task to user %s as they are not a member of team %s", 
                    assignedTo.getId(), team.getId()));
            }
            
            task.setAssignedTo(assignedTo);
        }

        try {
            task = taskRepository.save(task);
            log.info("Successfully created task with id: {}", task.getId());
            return mapToTaskResponse(task);
        } catch (Exception e) {
            log.error("Error creating task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create task: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks(String status, String search) {
        log.debug("Fetching tasks with status: {}, search: {}", status, search);
        
        List<Task> tasks;
        if (search != null && !search.trim().isEmpty()) {
            tasks = taskRepository.findByProjectIdAndStatus(search, search);
        } else if (status != null && !status.trim().isEmpty()) {
            tasks = taskRepository.findByStatus(status);
        } else {
            tasks = taskRepository.findAll();
        }

        return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(String taskId) {
        log.debug("Fetching task with id: {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        return mapToTaskResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(String taskId, UpdateTaskRequest request) {
        log.info("Updating task with id: {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            task.setTitle(request.getTitle().trim());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription().trim());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            task.setStatus(Task.TaskStatus.valueOf(request.getStatus().toUpperCase()));
        }

        task = taskRepository.save(task);
        log.info("Updated task with id: {}", taskId);
        return mapToTaskResponse(task);
    }

    @Transactional
    public TaskResponse assignTask(String taskId, String userId, String currentUserId) {
        log.info("Assigning task {} to user {}", taskId, userId);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        // Verify current user has permission (e.g., is team admin or task creator)
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        
        if (!task.getCreatedBy().equals(currentUser) && !task.getTeam().getCreatedBy().equals(currentUser)) {
            throw new SecurityException("You don't have permission to assign this task");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Verify user is a member of the team
        if (!task.getTeam().getMembers().contains(user)) {
            throw new IllegalStateException("User is not a member of the task's team");
        }

        task.setAssignedTo(user);
        task = taskRepository.save(task);
        log.info("Assigned task {} to user {}", taskId, userId);
        return mapToTaskResponse(task);
    }

    @Transactional
    public TaskResponse updateTaskStatus(String taskId, String status, String currentUserId) {
        log.info("Updating status of task {} to {}", taskId, status);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        // Verify current user is either assigned to the task or is the creator/team admin
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        
        boolean isAssignedUser = task.getAssignedTo() != null && task.getAssignedTo().equals(currentUser);
        boolean isCreatorOrAdmin = task.getCreatedBy().equals(currentUser) || 
                                 task.getTeam().getCreatedBy().equals(currentUser);
        
        if (!isAssignedUser && !isCreatorOrAdmin) {
            throw new SecurityException("You don't have permission to update this task's status");
        }

        try {
            Task.TaskStatus newStatus = Task.TaskStatus.valueOf(status.toUpperCase());
            task.setStatus(newStatus);
            task = taskRepository.save(task);
            log.info("Updated status of task {} to {}", taskId, newStatus);
            return mapToTaskResponse(task);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

    @Transactional
    public void deleteTask(String taskId, String currentUserId) {
        log.info("Deleting task with id: {}", taskId);
        
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        
        // Only allow task creator or team admin to delete
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        
        if (!task.getCreatedBy().equals(currentUser) && !task.getTeam().getCreatedBy().equals(currentUser)) {
            throw new SecurityException("You don't have permission to delete this task");
        }
        
        taskRepository.delete(task);
        log.info("Deleted task with id: {}", taskId);
    }

    private TaskResponse mapToTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus() != null ? task.getStatus().name() : null)
                .priority(task.getPriority() != null ? task.getPriority().name() : null)
                .teamId(task.getTeam() != null ? task.getTeam().getId() : null)
                .teamName(task.getTeam() != null ? task.getTeam().getName() : null)
                .assignedToId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                .assignedToName(task.getAssignedTo() != null ? 
                        String.format("%s %s", 
                            task.getAssignedTo().getFirstName(), 
                            task.getAssignedTo().getLastName()).trim() : null)
                .createdById(task.getCreatedBy() != null ? task.getCreatedBy().getId() : null)
                .createdByName(task.getCreatedBy() != null ? 
                        String.format("%s %s", 
                            task.getCreatedBy().getFirstName(), 
                            task.getCreatedBy().getLastName()).trim() : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .commentCount(task.getComments() != null ? task.getComments().size() : 0)
                .attachmentCount(task.getAttachments() != null ? task.getAttachments().size() : 0)
                .build();
    }
}
