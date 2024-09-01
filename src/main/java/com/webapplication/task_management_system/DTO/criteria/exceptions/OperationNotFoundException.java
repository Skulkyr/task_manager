package com.webapplication.task_management_system.DTO.criteria.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OperationNotFoundException extends RuntimeException {
    private String name;
}
