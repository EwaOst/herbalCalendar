package com.herbalcalendar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.herbalcalendar.security.SecurityConfig;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Import(SecurityConfig.class)
@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule()); // Dodaj moduł JavaTimeModule
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Wyłącz
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
}