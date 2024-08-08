package com.webapplication.task_management_system.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
public class TaskEditPermissionException extends RuntimeException {
    private final String message;
}
