package com.Assignment.Task_Tracker.Controller;

import com.Assignment.Task_Tracker.DTO.CreateTeamRequest;
import com.Assignment.Task_Tracker.DTO.TeamResponse;
import com.Assignment.Task_Tracker.Service.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TeamService teamService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateTeamRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateTeamRequest();
        request.setDescription("Test Description");
    }

    @Test
    void createTeam_returnsOk() throws Exception {
        TeamResponse mockResponse = new TeamResponse(
            "1L",
            request.getName(),
            request.getDescription(),
            "testuser",
            String.valueOf(List.of("testuser")),1
            LocalDateTime.now()
        );

        Mockito.when(teamService.createTeam(anyString(), any(CreateTeamRequest.class))).thenReturn(mockResponse);
        
        mockMvc.perform(post("/api/v1/teams")
                .param("creatorId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
