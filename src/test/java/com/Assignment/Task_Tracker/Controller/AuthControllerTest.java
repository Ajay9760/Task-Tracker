package com.Assignment.Task_Tracker.Controller;

package com.tasktracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.Assignment.Task_Tracker.DTO.LoginRequest;
import com.Assignment.Task_Tracker.DTO.RegisterRequest;
import com.Assignment.Task_Tracker.Service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest
public class AuthControllerTest {
    @Autowired private MockMvc mockMvc;
    @Mock
    private AuthService authService;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void register_returnsOk() throws Exception {
        Mockito.when(authService.register(any())).thenReturn(null);
        RegisterRequest req = new RegisterRequest();
        req.setUsername("u"); req.setEmail("a@b.com"); req.setPassword("pass123");
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void login_returnsOk() throws Exception {
        Mockito.when(authService.login(any())).thenReturn(null);
        LoginRequest req = new LoginRequest();
        req.setUsername("u"); req.setPassword("pass123");
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}
