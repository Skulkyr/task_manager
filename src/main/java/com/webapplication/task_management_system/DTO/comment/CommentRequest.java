package com.webapplication.task_management_system.DTO.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull(message = "The comment must be at least 5 characters long!")
    @Size(min = 5, message = "The comment must be at least 5 characters long!")
    private String comment;
}
