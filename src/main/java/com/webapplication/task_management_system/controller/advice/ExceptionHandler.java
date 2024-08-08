package com.webapplication.task_management_system.controller.advice;

import com.webapplication.task_management_system.exception.TaskEditPermissionException;
import com.webapplication.task_management_system.exception.UnauthorizedAccessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex, WebRequest webRequest) {
        log.info(STR."NoResourceFoundException: \{webRequest.getDescription(true)} \{ex.getMessage()}");
        ErrorResponse errorResponse = new ErrorResponse(System.currentTimeMillis(),
                HttpStatus.NOT_FOUND.value(), "NotFound", "The page was not found");

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest webRequest) {
        log.info(STR."BadCredentialsException: \{webRequest.getDescription(true)} \{ex.getMessage()}");
        ErrorResponse errorResponse = new ErrorResponse(System.currentTimeMillis(),
                HttpStatus.FORBIDDEN.value(), "Forbidden", "Invalid user credentials");

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex, WebRequest webRequest) {
        log.info(STR."UnauthorizedAccessException: \{webRequest.getDescription(true)} \{ex.getMessage()}");
        ErrorResponse errorResponse = new ErrorResponse(System.currentTimeMillis(),
                HttpStatus.UNAUTHORIZED.value(), "Unauthorized", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest webRequest) {
        log.info(STR."IllegalArgumentException: \{webRequest.getDescription(true)} \{ex.getMessage()}");
        ErrorResponse errorResponse = new ErrorResponse(System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(), "BadRequest", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({TaskEditPermissionException.class})
    public ResponseEntity<ErrorResponse> handleTaskEditPermissionException(TaskEditPermissionException ex, WebRequest webRequest) {
        log.info(STR."TaskEditPermissionException: \{webRequest.getDescription(true)} \{ex.getMessage()}");
        ErrorResponse errorResponse = new ErrorResponse(System.currentTimeMillis(),
                HttpStatus.FORBIDDEN.value(), "Forbidden", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest webRequest) {
        log.info(STR."HttpRequestMethodNotSupportedException: \{webRequest.getDescription(true)} \{ex.getMessage()}");
        ErrorResponse errorResponse = new ErrorResponse(System.currentTimeMillis(),
                HttpStatus.METHOD_NOT_ALLOWED.value(), "Forbidden", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(Exception ex, WebRequest webRequest) {
        log.info(STR."UnknownException: \{webRequest.getDescription(true)} \{ex.getMessage()}");
        ErrorResponse errorResponse = new ErrorResponse(System.currentTimeMillis(),
                520, "Unknown", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @Setter
    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private long timestamp;
        private int status;
        private String error;
        private String message;

    }
}

