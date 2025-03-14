package com.herbalcalendar.controller;

import com.herbalcalendar.dto.JwtAuthenticationResponse;
import com.herbalcalendar.dto.LoginRequest;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.security.JwtTokenProvider;
import com.herbalcalendar.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private AuthController authController;

    @Test
    void login_WhenUserExistsAndPasswordMatches_ShouldReturnToken() {
        // GIVEN
        LoginRequest loginRequest = new LoginRequest("Ewa", "password123");
        UserModel user = new UserModel();
        user.setUsername("Ewa");
        user.setPassword("$2a$10$..."); // Zakodowane hasło

        when(userService.authenticate("Ewa", "password123")).thenReturn(user);
        when(jwtTokenProvider.generateToken("Ewa")).thenReturn("generatedToken");

        // WHEN
        ResponseEntity<JwtAuthenticationResponse> response = authController.login(loginRequest);

        // THEN
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("generatedToken", response.getBody().getToken());

        verify(userService).authenticate("Ewa", "password123");
        verify(jwtTokenProvider).generateToken("Ewa");
    }
}