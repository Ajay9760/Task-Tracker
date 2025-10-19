package com.Assignment.Task_Tracker.Service;

import com.Assignment.Task_Tracker.DTO.*;
import com.Assignment.Task_Tracker.Entity.Comment;
import com.Assignment.Task_Tracker.Entity.Task;
import com.Assignment.Task_Tracker.Entity.Team;
import com.Assignment.Task_Tracker.Entity.User;
import com.Assignment.Task_Tracker.Exception.ResourceNotFoundException;
import com.Assignment.Task_Tracker.Exception.BadRequestException;
import com.Assignment.Task_Tracker.Repository.CommentRepository;
import com.Assignment.Task_Tracker.Repository.TaskRepository;
import com.Assignment.Task_Tracker.Repository.TeamRepository;
import com.Assignment.Task_Tracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing teams and related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    /**
     * Creates a new team with the given user as the creator and first member.
     *
     * @param userId  the ID of the user creating the team
     * @param request the team creation request containing name and description
     * @return the created team response
     * @throws ResourceNotFoundException if the user is not found
     * @throws BadRequestException if the team name is already taken
     */
    @Transactional
    public TeamResponse createTeam(String userId, CreateTeamRequest request) {
        log.info("Creating new team with name: {}", request.getName());
        
        validateTeamRequest(request);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (teamRepository.existsByName(request.getName())) {
            log.warn("Team creation failed - Team name already exists: {}", request.getName());
            throw new BadRequestException("Team name already exists: " + request.getName());
        }

        Team team = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(user)
                .build();
        
        team.getMembers().add(user);
        team = teamRepository.save(team);
        
        log.info("Team created successfully with ID: {}", team.getId());
        return mapToTeamResponse(team);
    }

    /**
     * Adds a user as a member to the specified team.
     *
     * @param teamId  the ID of the team
     * @param request the request containing the user ID to add
     * @return the updated team response
     * @throws ResourceNotFoundException if team or user is not found
     * @throws BadRequestException if the user is already a member of the team
     */
    @Transactional
    public TeamResponse addMember(String teamId, AddMemberRequest request) {
        log.info("Adding member {} to team {}", request.getUserId(), teamId);
        
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + teamId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        if (team.getMembers().contains(user)) {
            log.warn("User {} is already a member of team {}", user.getId(), teamId);
            throw new BadRequestException("User is already a member of this team");
        }

        team.getMembers().add(user);
        team = teamRepository.save(team);
        
        log.info("User {} added to team {}", user.getId(), teamId);
        return mapToTeamResponse(team);
    }

    /**
     * Retrieves all tasks associated with a team.
     *
     * @param teamId the ID of the team
     * @return list of task responses
     * @throws ResourceNotFoundException if the team is not found
     */
    public List<TaskResponse> getTeamTasks(String teamId) {
        log.debug("Fetching tasks for team: {}", teamId);
        
        if (!teamRepository.existsById(teamId)) {
            log.warn("Team not found with ID: {}", teamId);
            throw new ResourceNotFoundException("Team not found with ID: " + teamId);
        }

        return taskRepository.findByTeamId(teamId).stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps a Task entity to a TaskResponse DTO.
     *
     * @param task the task entity to map
     * @return the mapped TaskResponse
     */
    private TaskResponse mapToTaskResponse(Task task) {
        if (task == null) {
            log.warn("Task is null, cannot map to TaskResponse");
            return null;
        }

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus() != null ? task.getStatus().name() : Task.TaskStatus.OPEN.name())
                .priority(task.getPriority() != null ? task.getPriority().name() : Task.Priority.MEDIUM.name())
                .teamId(task.getTeam() != null ? task.getTeam().getId() : null)
                .teamName(task.getTeam() != null ? task.getTeam().getName() : null)
                .assignedToId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                .assignedToName(task.getAssignedTo() != null ? 
                    (task.getAssignedTo().getFirstName() + " " + task.getAssignedTo().getLastName()).trim() : null)
                .createdById(task.getCreatedBy() != null ? task.getCreatedBy().getId() : null)
                .createdByName(task.getCreatedBy() != null ? 
                    (task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getLastName()).trim() : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .attachments(mapAttachments(task))
                .build();
    }
    
    /**
     * Maps task attachments to attachment responses.
     *
     * @param task the task containing attachments
     * @return list of attachment responses, or empty list if no attachments
     */
    private Set<AttachmentResponse> mapAttachments(Task task) {
        if (task.getAttachments() == null || task.getAttachments().isEmpty()) {
            return new HashSet<>();
        }
        
        return task.getAttachments().stream()
                .map(attachment -> AttachmentResponse.builder()
                        .id(attachment.getId())
                        .fileName(attachment.getFileName())
                        .fileType(attachment.getFileType())
                        .fileUrl(attachment.getFileUrl())
                        .createdAt(attachment.getUploadedAt())
                        .build())
                .collect(Collectors.toSet());
    }
    
    /**
     * Adds a comment to a specific task.
     *
{{ ... }}
     * @param userId the ID of the user adding the comment
     * @param userId  the ID of the user adding the comment
     * @param request the comment creation request
     * @return the created comment response
     * @throws ResourceNotFoundException if task or user is not found
     * @throws BadRequestException if the comment content is empty
     */
    @Transactional
    public CommentResponse addComment(String taskId, String userId, CreateCommentRequest request) {
        log.info("Adding comment to task {} by user {}", taskId, userId);
        
        if (!StringUtils.hasText(request.getContent())) {
            throw new BadRequestException("Comment content cannot be empty");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .task(task)
                .user(user)
                .build();

        comment = commentRepository.save(comment);
        log.debug("Comment added with ID: {}", comment.getId());
        
        return mapToCommentResponse(comment);
    }

    /**
     * Retrieves all comments for a specific task.
     *
     * @param taskId the ID of the task
     * @return list of comment responses
     * @throws ResourceNotFoundException if the task is not found
     */
    public List<CommentResponse> getTaskComments(String taskId) {
        log.debug("Fetching comments for task: {}", taskId);
        
        if (!taskRepository.existsById(taskId)) {
            log.warn("Task not found with ID: {}", taskId);
            throw new ResourceNotFoundException("Task not found with ID: " + taskId);
        }

        return commentRepository.findByTaskId(taskId).stream()
                .filter(Objects::nonNull)
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps a Comment entity to a CommentResponse DTO.
     *
     * @param comment the comment entity to map
     * @return the mapped CommentResponse
     */
    private CommentResponse mapToCommentResponse(Comment comment) {
        if (comment == null) {
            return null;
        }
        
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser() != null ? comment.getUser().getId() : null)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    /**
     * Maps a Team entity to a TeamResponse DTO.
     *
     * @param team the team entity to map
     * @return the mapped TeamResponse
     */
    private TeamResponse mapToTeamResponse(Team team) {
        if (team == null) {
            return null;
        }
        
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .createdById(team.getCreatedBy() != null ? team.getCreatedBy().getId() : null)
                .memberCount(team.getMembers() != null ? team.getMembers().size() : 0)
                .createdAt(team.getCreatedAt())
                .updatedAt(team.getUpdatedAt())
                .build();
    }
    
    /**
     * Validates the team creation/update request.
     *
     * @param request the team request to validate
     * @throws BadRequestException if the request is invalid
     */
    private void validateTeamRequest(CreateTeamRequest request) {
        if (request == null) {
            throw new BadRequestException("Team request cannot be null");
        }
        
        if (!StringUtils.hasText(request.getName())) {
            throw new BadRequestException("Team name is required");
        }
        
        if (request.getName().length() > 100) {
            throw new BadRequestException("Team name must be less than 100 characters");
        }
    }
    public List<TaskResponse> getAllTasks(String status, String search) {
        log.debug("Fetching tasks with status: {}, search: {}", status, search);

        List<Task> tasks;
        if (search != null && !search.trim().isEmpty()) {
            tasks = taskRepository.searchTasks(search);
        } else if (status != null && !status.trim().isEmpty()) {
            try {
                Task.TaskStatus taskStatus = Task.TaskStatus.valueOf(status.toUpperCase());
                tasks = taskRepository.findByStatus(Task.TaskStatus.valueOf(String.valueOf(taskStatus)));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid status value: " + status);
            }
        } else {
            tasks = taskRepository.findAll();
        }

        return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }
}
