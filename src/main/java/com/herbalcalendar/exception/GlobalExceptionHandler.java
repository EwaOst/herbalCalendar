package com.herbalcalendar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

    // Obsługuje HerbNotFoundException
    @ExceptionHandler(HerbNotFoundException.class)
    public ResponseEntity<String> handleHerbNotFoundException(HerbNotFoundException ex) {
        // Zwraca odpowiedź z kodem HTTP 404 (Not Found) oraz wiadomością wyjątku
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
    }

    // Obsługuje inne ogólne wyjątki
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
