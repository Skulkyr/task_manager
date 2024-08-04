package com.webapplication.task_management_system.controller.advice;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.info(STR."NoResourceFoundException: \{ex.getMessage()}");
        ErrorResponse errorResponse = new ErrorResponse(System.currentTimeMillis(),
                HttpStatus.NOT_FOUND.value(), "NotFound", "Страница не найдена");

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.info(STR."BadCredentialsException: \{ex.getMessage()}");
        ErrorResponse errorResponse = new ErrorResponse(System.currentTimeMillis(),
                HttpStatus.FORBIDDEN.value(), "Forbidden", "Неверные учетные данные пользователя");

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(Exception ex) {
        log.info(STR."UnknownException: \{ex.getMessage()}");
        ErrorResponse errorResponse = new ErrorResponse(System.currentTimeMillis(),
                520, "Unknown", "Неизвестная ошибка");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @Setter
    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private long timestamp;
        private int status;
        private String error;
        private String trace;

    }
}

