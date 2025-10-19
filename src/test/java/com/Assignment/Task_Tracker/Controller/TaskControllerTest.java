package com.Assignment.Task_Tracker.Controller;

import com.Assignment.Task_Tracker.DTO.TaskResponse;
import com.Assignment.Task_Tracker.Service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllTasks_returnsOk() throws Exception {
        // Create a TaskResponse DTO (not Entity)
        TaskResponse taskResponse = TaskResponse.builder()
                .id("test-id")
                .title("Test Task")
                .description("Test Description")
                .status("OPEN")
                .priority("MEDIUM")
                .teamId("team-1")
                .teamName("Test Team")
                .createdById("user-1")
                .createdByName("Test User")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .commentCount(0)
                .attachmentCount(0)
                .build();

        // Mock service to return TaskResponse list
        when(taskService.getAllTasks(anyString(), anyString()))
                .thenReturn(List.of(taskResponse));

        // Perform the request
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("test-id"))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    @WithMockUser
    void getAllTasks_withStatusFilter_returnsOk() throws Exception {
        TaskResponse taskResponse = TaskResponse.builder()
                .id("test-id")
                .title("Test Task")
                .description("Test Description")
                .status("IN_PROGRESS")
                .priority("HIGH")
                .teamId("team-1")
                .teamName("Test Team")
                .createdById("user-1")
                .createdByName("Test User")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .commentCount(0)
                .attachmentCount(0)
                .build();

        when(taskService.getAllTasks(anyString(), anyString()))
                .thenReturn(List.of(taskResponse));

        mockMvc.perform(get("/api/v1/tasks")
                        .param("status", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"));
    }

    @Test
    @WithMockUser
    void getAllTasks_withSearchFilter_returnsOk() throws Exception {
        TaskResponse taskResponse = TaskResponse.builder()
                .id("test-id")
                .title("Searchable Task")
                .description("This task is searchable")
                .status("OPEN")
                .priority("MEDIUM")
                .teamId("team-1")
                .teamName("Test Team")
                .createdById("user-1")
                .createdByName("Test User")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .commentCount(0)
                .attachmentCount(0)
                .build();

        when(taskService.getAllTasks(anyString(), anyString()))
                .thenReturn(List.of(taskResponse));

        mockMvc.perform(get("/api/v1/tasks")
                        .param("search", "Searchable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Searchable Task"));
    }

    @Test
    @WithMockUser
    void getTask_returnsOk() throws Exception {
        TaskResponse taskResponse = TaskResponse.builder()
                .id("test-id")
                .title("Test Task")
                .description("Test Description")
                .status("OPEN")
                .priority("MEDIUM")
                .teamId("team-1")
                .teamName("Test Team")
                .createdById("user-1")
                .createdByName("Test User")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .commentCount(0)
                .attachmentCount(0)
                .build();

        when(taskService.getTaskById(anyString()))
                .thenReturn(taskResponse);

        mockMvc.perform(get("/api/v1/tasks/test-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-id"))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }
}