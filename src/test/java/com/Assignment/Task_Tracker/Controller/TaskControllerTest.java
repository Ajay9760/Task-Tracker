package com.Assignment.Task_Tracker.Controller;



import com.Assignment.Task_Tracker.Entity.Team;
import com.Assignment.Task_Tracker.Entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Assignment.Task_Tracker.Entity.Task;
import com.Assignment.Task_Tracker.Service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired private MockMvc mockMvc;
    @Mock
    private TaskService taskService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void getAllTasks_returnsOk() throws Exception {
        // Create a mock user
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        
        // Create a mock team
        Team team = new Team();
        team.setId("team-1");
        team.setName("Test Team");
        
        // Create a test task with required fields
        Task task = new Task();
        task.setId("test-id");
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setProject(team);
        task.setCreatedBy(user);
        
        when(taskService.getAllTasks()).thenReturn(List.of(task));
        mockMvc.perform(get("/api/v1/tasks"))
               .andExpect(status().isOk());
    }
}

