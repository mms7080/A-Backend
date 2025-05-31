package com.example.demo.Booking.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomBookingException extends RuntimeException {
    private final HttpStatus httpStatus;

    public CustomBookingException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}