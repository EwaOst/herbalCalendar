package com.herbalcalendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herbalcalendar.enums.NotificationPreference;
import com.herbalcalendar.security.JwtTokenProvider;
import com.herbalcalendar.security.SecurityConfig;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)  // Kluczowa linia!
@Import({SecurityConfig.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Wstrzykiwane przez Springa

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;  // Zmienione z @Mock na @MockBean

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        Long userId = 1L;
        String username = "testUser";

        // Mockowanie zachowania JwtTokenProvider
        Mockito.when(jwtTokenProvider.generateToken(userId, username)).thenReturn("mockToken123");
        Mockito.when(jwtTokenProvider.getUserIdFromToken("mockToken123")).thenReturn(userId);
    }

    @Test
    void getAllUsers_ShouldReturnUserListAndStatusOk() throws Exception {
        UserModel user1 = new UserModel();
        UserModel user2 = new UserModel();
        List<UserModel> users = List.of(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    void createUser_ShouldCreateUserAndReturnCreatedStatus() throws Exception {
        UserModel user = new UserModel();
        UserModel savedUser = new UserModel();

        when(userService.createUser(any(UserModel.class))).thenReturn(savedUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(savedUser)));

    }

    @Test
    void updateUser_ShouldUpdateUserAndReturnNewHerb() throws Exception {
        UserModel user = new UserModel();
        UserModel newUser = new UserModel();

        when(userService.updateUser(any(Long.class), any(UserModel.class))).thenReturn(Optional.of(newUser));

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newUser)));
    }

    @Test
    void getUserById_ShouldFindUserWithProvidedId() throws Exception {
        UserModel foundUser = new UserModel();
        foundUser.setId(1L);

        when(userService.getUserById(any(Long.class))).thenReturn(Optional.of(foundUser));

        mockMvc.perform(get("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(foundUser)));
    }

    @Test
    void getUserById_ShouldReturnNotFoundOrNonExistingHerb() throws Exception {

        when(userService.getUserById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteUser_ShouldDeleteWithProvidedId() throws Exception {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(any(Long.class));

        mockMvc.perform(delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateNotificationPreference_ShouldReturnOk_WhenTokenIsValid() throws Exception {
        // Given
        String validToken = "Bearer validToken";
        Long userId = 1L;
        NotificationPreference preference = NotificationPreference.EMAIL;

        Mockito.when(userService.getUserIdFromToken(validToken)).thenReturn(userId);
        Mockito.doNothing().when(userService).updateNotificationPreference(userId, preference);

        // When & Then
        mockMvc.perform(put("/users/notification-preference")
                        .param("preference", preference.name())
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification preference updated to " + preference));
    }

    @Test
    void updateNotificationPreference_ShouldReturnError_WhenTokenIsInvalid() throws Exception {
        // Given
        String invalidAuthToken  = "Bearer invalidToken";
        NotificationPreference preference = NotificationPreference.EMAIL;

        Mockito.when(userService.getUserIdFromToken(invalidAuthToken)).thenReturn(null);

        // When & Then
        mockMvc.perform(put("/users/notification-preference")
                        .param("preference", preference.name())
                        .header("Authorization", invalidAuthToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addHerbToUser_ShouldReturnOk() throws Exception {
        UserModel mockUser = new UserModel();
        Mockito.when(userService.addHerbToUser(1L, 1L)).thenReturn(mockUser);

        mockMvc.perform(post("/users/1/herbs/1"))
                .andExpect(status().isOk());
    }
}