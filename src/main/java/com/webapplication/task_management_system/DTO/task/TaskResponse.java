package com.webapplication.task_management_system.DTO.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "First Task")
    private String title;
    @Schema(example = "This is the first task description")
    private String description;
    @Schema(example = "john.doe@email.com")
    private String authorEmail;
    @Schema(example = "jane.smith@email.com")
    private String executorEmail;
    @Schema(example = "low")
    private String priority;
    @Schema(example = "in process")
    private String status;
    @Schema(example = "2024-08-27T10:23:56.088237")
    private String createDate;

}
