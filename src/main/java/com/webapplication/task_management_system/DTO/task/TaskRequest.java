package com.webapplication.task_management_system.DTO.task;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRequest {

    @NotNull(message = "The title must contain from 6 to 40 characters!")
    @Size(min = 6, max = 40, message = "The title must contain from 6 to 40 characters!")
    private String title;

    @NotNull(message = "The description must contain from 40 to 2000 characters")
    @Size(min = 40, max = 2000, message = "The description must contain from 40 to 2000 characters")
    private String description;

    @Email
    private String executorEmail;
    private String priority;
    private String status;

}
