package com.herbalcalendar.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.herbalcalendar.enums.NotificationPreference;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.repository.UserRepository;
import com.herbalcalendar.security.JwtTokenProvider;
import com.herbalcalendar.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserModel> createUser(
            @Valid @RequestBody UserModel userModel) {
        UserModel newUser = userService.createUser(userModel);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserModel> updateUser(
            @PathVariable Long id,
            @RequestBody UserModel user) {
        return userService.updateUser(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(
            @PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/{userId}/herbs/{herbId}")
    public ResponseEntity<UserModel> addHerbToUser(@PathVariable Long userId, @PathVariable Long herbId) {
        UserModel updatedUser = userService.addHerbToUser(userId, herbId);

        // Dodanie logowania
        System.out.println("Returned user: " + updatedUser);

        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/notification-preference")
    public ResponseEntity<String> updateNotificationPreference(
            @RequestParam NotificationPreference preference,
            @RequestHeader("Authorization") String token) {

        Long userIdFromToken = userService.getUserIdFromToken(token);

        // Dodaj sprawdzenie czy użytkownik istnieje
        if (userIdFromToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        userService.updateNotificationPreference(userIdFromToken, preference);
        return ResponseEntity.ok("Notification preference updated to " + preference);
    }
}
