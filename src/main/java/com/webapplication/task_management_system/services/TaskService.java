package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.DTO.criteria.SearchDTO;
import com.webapplication.task_management_system.entity.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TaskService {

    Task saveTask(Task task);

    void deleteTaskFromId(Long id);

    Task getTaskById(Long id);

    Page<Task> getPageSortTasks(SearchDTO searchDTO);
}
