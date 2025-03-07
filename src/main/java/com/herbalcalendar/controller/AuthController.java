package com.herbalcalendar.controller;

import com.herbalcalendar.dto.JwtAuthenticationResponse;
import com.herbalcalendar.dto.LoginRequest;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.security.JwtTokenProvider;
import com.herbalcalendar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 1. Sprawdź, czy użytkownik istnieje i czy hasło jest poprawne
        UserModel user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        // 2. Wygeneruj token JWT
        String token = jwtTokenProvider.generateToken(user.getUsername());

        // 3. Zwróć token w odpowiedzi
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }
}
