package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.DTO.task.TaskDto;
import com.webapplication.task_management_system.entity.task.Task;
import com.webapplication.task_management_system.entity.user.User;
import com.webapplication.task_management_system.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task createTask(TaskDto taskDto) {
        return null;
    }
}
