package com.Assignment.Task_Tracker.Controller;

import com.Assignment.Task_Tracker.DTO.CreateTaskRequest;
import com.Assignment.Task_Tracker.DTO.MessageResponse;
import com.Assignment.Task_Tracker.DTO.TaskResponse;
import com.Assignment.Task_Tracker.DTO.UpdateTaskRequest;
import com.Assignment.Task_Tracker.Service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(taskService.getAllTasks(status, search));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable String taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable String taskId,
            @Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }

    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable String taskId,
            @RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        return ResponseEntity.ok(taskService.assignTask(taskId, userId, body.get("currentUserId")));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateStatus(
            @PathVariable String taskId,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, status,body.get("currentUserId")));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<MessageResponse> deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId,("currentUserId"));
        return ResponseEntity.ok(new MessageResponse("Task deleted successfully"));
    }
}