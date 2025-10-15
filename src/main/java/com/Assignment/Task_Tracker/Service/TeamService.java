package com.Assignment.Task_Tracker.Service;

import com.Assignment.Task_Tracker.DTO.*;
import com.Assignment.Task_Tracker.Entity.Comment;
import com.Assignment.Task_Tracker.Entity.Task;
import com.Assignment.Task_Tracker.Entity.Team;
import com.Assignment.Task_Tracker.Entity.User;
import com.Assignment.Task_Tracker.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public TeamResponse createTeam(String userId, CreateTeamRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (teamRepository.existsByName(request.getName())) {
            throw new RuntimeException("Team name already exists");
        }

        Team team = new Team();
        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team.setCreatedBy(user);
        team.getMembers().add(user);

        team = teamRepository.save(team);
        return mapToTeamResponse(team);
    }

    public TeamResponse addMember(String teamId, AddMemberRequest request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        team.getMembers().add(user);
        team = teamRepository.save(team);
        return mapToTeamResponse(team);
    }

    public List<TaskResponse> getTeamTasks(String teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new RuntimeException("Team not found");
        }

        List<Task> tasks = taskRepository.findByProjectId(teamId);
        return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse addComment(String taskId, String userId, CreateCommentRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setUser(user);

        comment = commentRepository.save(comment);
        return mapToCommentResponse(comment);
    }

    public List<CommentResponse> getTaskComments(String taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found");
        }

        List<Comment> comments = commentRepository.findByTaskId(taskId);
        return comments.stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }

    private TeamResponse mapToTeamResponse(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedBy().getId(),
                team.getCreatedBy().getUsername(),
                team.getCreatedAt(),
                team.getMembers().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet())
        );
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

    private CommentResponse mapToCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getCreatedAt()
        );
    }
}