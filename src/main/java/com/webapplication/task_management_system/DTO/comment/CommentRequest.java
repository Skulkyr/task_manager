package com.webapplication.task_management_system.DTO.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull(message = "The comment must be at least 5 characters long!")
    @Size(min = 5, message = "The comment must be at least 5 characters long!")
    @Schema(example = "This is a comment on the first task by John")
    private String comment;
}
