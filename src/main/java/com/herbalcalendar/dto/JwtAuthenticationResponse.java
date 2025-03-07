package com.herbalcalendar.dto;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationResponse {
    private String token;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    // Getter

}
