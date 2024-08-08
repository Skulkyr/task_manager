package com.webapplication.task_management_system.DTO.task;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskStatusRequest {
    @NotNull
    private String status;
}
