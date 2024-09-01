package com.webapplication.task_management_system.DTO.criteria.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IncorrectCriteriaOperationException extends RuntimeException {
    String description;
}
