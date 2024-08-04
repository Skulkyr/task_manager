package com.webapplication.task_management_system.DTO.task;

import com.webapplication.task_management_system.entity.task.Priority;
import com.webapplication.task_management_system.entity.task.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
public class TaskDto {
    @Size(min = 6, max = 40, message = "Заголовок должен содержать от 6 до 40 символов!")
    private String title;
    @Size(min = 40, max = 2000, message = "Описание должно содержать от 40 до 2000 символов")
    private String description;
    private String authorEmail;
    private String executorEmail;
    @NotNull
    private Priority priority;

}
