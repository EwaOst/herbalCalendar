package com.herbalcalendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herbalcalendar.dto.LoginRequest;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.security.JwtTokenProvider;
import com.herbalcalendar.security.SecurityConfig;
import com.herbalcalendar.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;


    @Test
    void login_WhenUserExistsAndPasswordMatches_ShouldReturnToken() throws Exception {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest("Ewa", "password123");
        UserModel user = new UserModel();
        user.setId(1L);
        user.setUsername("Ewa");
        user.setPassword("$2a$10$..."); // Zakodowane hasło

        when(userService.authenticate("Ewa", "password123")).thenReturn(user);
        when(jwtTokenProvider.generateToken(1L, "Ewa")).thenReturn("generatedToken");

        // WHEN & THEN
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("generatedToken"));

        verify(userService).authenticate("Ewa", "password123");
        verify(jwtTokenProvider).generateToken(1L, "Ewa");
    }

    @Test
    void login_WhenUserDoesNotExist_ShouldReturnUnauthorized() throws Exception {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest("NonExistentUser", "password123");

        when(userService.authenticate("NonExistentUser", "password123")).thenReturn(null);

        // WHEN & THEN
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

        verify(userService).authenticate("NonExistentUser", "password123");
    }

    @Test
    void login_WhenAuthenticationThrowsException_ShouldReturnInternalServerError() throws Exception {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest("Ewa", "password123");

        when(userService.authenticate("Ewa", "password123")).thenThrow(new RuntimeException("Authentication failed"));

        // WHEN & THEN
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Authentication failed: Authentication failed"));
    }
}