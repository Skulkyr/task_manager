package com.webapplication.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskRequest {
    @Schema(example = "First Task")
    @NotNull(message = "The title must contain from 6 to 40 characters!")
    @Size(min = 6, max = 40, message = "The title must contain from 6 to 40 characters!")
    private String title;

    @Schema(example = "This is the first task description")
    @NotNull(message = "The description must contain from 40 to 2000 characters")
    @Size(min = 40, max = 2000, message = "The description must contain from 40 to 2000 characters")
    private String description;

    @Schema(example = "jane.smith@email.com", description = "The existence of a user with this address is checked")
    @Email
    private String executorEmail;
    @Schema(example = "low")
    private String priority;
    @Schema(example = "in process")
    private String status;

}
