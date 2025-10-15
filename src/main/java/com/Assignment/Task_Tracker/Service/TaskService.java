package com.Assignment.Task_Tracker.Service;

import com.Assignment.Task_Tracker.DTO.CreateTaskRequest;
import com.Assignment.Task_Tracker.DTO.TaskResponse;
import com.Assignment.Task_Tracker.DTO.UpdateTaskRequest;
import com.Assignment.Task_Tracker.Entity.Task;
import com.Assignment.Task_Tracker.Entity.Team;
import com.Assignment.Task_Tracker.Entity.User;
import com.Assignment.Task_Tracker.Repository.TaskRepository;
import com.Assignment.Task_Tracker.Repository.TeamRepository;
import com.Assignment.Task_Tracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService
{

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public TaskResponse createTask(String userId, CreateTaskRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Team project = teamRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setPriority(request.getPriority());
        task.setProject(project);
        task.setCreatedBy(user);

        if (request.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            task.setAssignedTo(assignedTo);
        }

        task = taskRepository.save(task);
        return mapToTaskResponse(task);
    }

    public List<TaskResponse> getAllTasks(String status, String search) {
        List<Task> tasks;

        if (search != null && !search.isEmpty()) {
            tasks = taskRepository.searchTasks(search);
        } else if (status != null && !status.isEmpty()) {
            tasks = taskRepository.findByStatus(status);
        } else {
            tasks = taskRepository.findAll();
        }

        return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return mapToTaskResponse(task);
    }

    public TaskResponse updateTask(String taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getPriority() != null) task.setPriority(request.getPriority());

        task = taskRepository.save(task);
        return mapToTaskResponse(task);
    }

    public TaskResponse assignTask(String taskId, String userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setAssignedTo(user);
        task = taskRepository.save(task);
        return mapToTaskResponse(task);
    }

    public TaskResponse updateTaskStatus(String taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(status);
        task = taskRepository.save(task);
        return mapToTaskResponse(task);
    }

    public void deleteTask(String taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(taskId);
    }

    private TaskResponse mapToTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.getPriority(),
                task.getProject().getId(),
                task.getProject().getName(),
                task.getAssignedTo() != null ? task.getAssignedTo().getId() : null,
                task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null,
                task.getCreatedBy().getId(),
                task.getCreatedBy().getUsername(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
