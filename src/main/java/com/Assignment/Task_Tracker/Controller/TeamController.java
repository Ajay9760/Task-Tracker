package com.Assignment.Task_Tracker.Controller;

import com.Assignment.Task_Tracker.DTO.*;
import com.Assignment.Task_Tracker.Service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(
            @Valid @RequestBody CreateTeamRequest request,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamService.createTeam(userId, request));
    }

    @PostMapping("/{teamId}/members")
    public ResponseEntity<TeamResponse> addMember(
            @PathVariable String teamId,
            @Valid @RequestBody AddMemberRequest request) {
        return ResponseEntity.ok(teamService.addMember(teamId, request));
    }

    @GetMapping("/{teamId}/tasks")
    public ResponseEntity<List<TaskResponse>> getTeamTasks(@PathVariable String teamId) {
        return ResponseEntity.ok(teamService.getTeamTasks(teamId));
    }

    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable String taskId,
            @Valid @RequestBody CreateCommentRequest request,
            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamService.addComment(taskId, userId, request));
    }

    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<List<CommentResponse>> getTaskComments(@PathVariable String taskId) {
        return ResponseEntity.ok(teamService.getTaskComments(taskId));
    }
}