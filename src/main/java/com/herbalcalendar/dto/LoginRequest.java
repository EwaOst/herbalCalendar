package com.herbalcalendar.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
public class LoginRequest {
    private String username;
    private String password;
}
