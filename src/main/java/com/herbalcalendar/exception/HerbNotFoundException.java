package com.herbalcalendar.exception;

public class HerbNotFoundException extends RuntimeException {
    public HerbNotFoundException(String message) {
        super(message);
    }
}
