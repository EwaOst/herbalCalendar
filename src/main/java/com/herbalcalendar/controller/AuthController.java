package com.herbalcalendar.controller;

import com.herbalcalendar.dto.JwtAuthenticationResponse;
import com.herbalcalendar.dto.LoginRequest;
import com.herbalcalendar.exception.*;
import com.herbalcalendar.model.UserModel;
import com.herbalcalendar.security.JwtTokenProvider;
import com.herbalcalendar.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, @Lazy JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        // 1. Sprawdź, czy użytkownik istnieje i czy hasło jest poprawne
        UserModel user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        // 2. Jeśli użytkownik nie istnieje lub hasło jest niepoprawne, zwróć UNAUTHORIZED
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 3. Wygeneruj token JWT
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());

        // 4. Zwróć token w odpowiedzi
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }
    @GetMapping("/some-endpoint")
    public String someMethod() throws HerbNotFoundException, UserAlreadyExistsException, NotificationException, JwtAuthenticationException, InvalidTokenException {
        // Ta metoda może rzucać różne wyjątki w zależności od potrzeb testowych
        throw new HerbNotFoundException("Herb not found");
    }
}
