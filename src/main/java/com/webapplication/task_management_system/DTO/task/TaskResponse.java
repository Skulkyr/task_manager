package com.webapplication.task_management_system.DTO.task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String authorEmail;
    private String executorEmail;
    private String priority;
    private String status;
    private String createDate;

}
