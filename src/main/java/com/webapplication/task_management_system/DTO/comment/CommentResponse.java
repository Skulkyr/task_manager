package com.webapplication.task_management_system.DTO.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "This is a comment on the first task by John")
    private String comment;
    @Schema(example = "john.doe@email.com")
    private String authorEmail;
    @Schema(example = "1")
    private Long taskId;
    @Schema(example = "2024-08-27T10:23:56.091352")
    private String createDate;
}
