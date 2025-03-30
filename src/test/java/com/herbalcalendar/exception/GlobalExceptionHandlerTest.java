package com.herbalcalendar.exception;

import com.herbalcalendar.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void handleHerbNotFoundException_ShouldReturnNotFound() throws Exception {
        when(authController.someMethod()).thenThrow(new HerbNotFoundException("Herb not found"));
        mockMvc.perform(get("/auth/some-endpoint"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Herb not found"));
    }

    @Test
    void handleUserAlreadyExistsException_ShouldReturnConflict() throws Exception {
        when(authController.someMethod()).thenThrow(new UserAlreadyExistsException("User already exists"));
        mockMvc.perform(get("/auth/some-endpoint"))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already exists"));
    }

    @Test
    void handleNotificationException_ShouldReturnInternalServerError() throws Exception {
        when(authController.someMethod()).thenThrow(new NotificationException("Failed to send notification"));
        mockMvc.perform(get("/auth/some-endpoint"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Notification error: Failed to send notification"));
    }

    @Test
    void handleJwtException_ShouldReturnUnauthorized() throws Exception {
        when(authController.someMethod()).thenThrow(new JwtAuthenticationException("Invalid token"));
        mockMvc.perform(get("/auth/some-endpoint"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized: Invalid token"));
    }

    @Test
    void handleRuntimeException_ShouldReturnInternalServerError() throws Exception {
        when(authController.someMethod()).thenThrow(new RuntimeException("Something went wrong"));
        mockMvc.perform(get("/auth/some-endpoint"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Authentication failed: Something went wrong"));
    }

    @Test
    void handleInvalidTokenException_ShouldReturnUnauthorized() throws Exception {
        when(authController.someMethod()).thenThrow(new InvalidTokenException("Invalid token"));
        mockMvc.perform(get("/auth/some-endpoint"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid token"));
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() throws Exception {
        when(authController.someMethod()).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/auth/some-endpoint"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Authentication failed: Unexpected error"));
    }
}
