package com.webapplication.task_management_system.DTO.comment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private String comment;
    private String authorEmail;
    private Long taskId;
    private String createDate;
}
