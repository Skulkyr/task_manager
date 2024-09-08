package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.DTO.criteria.SearchDTO;
import com.webapplication.task_management_system.entity.task.Task;
import org.springframework.data.domain.Page;


/**
 * The interface Task service.
 */
public interface TaskService {

    /**
     * Save task task.
     *
     * @param task the task
     * @return the task
     */
    Task saveTask(Task task);

    /**
     * Delete task from id.
     *
     * @param id the id
     */
    void deleteTaskFromId(Long id);

    /**
     * Gets task by id.
     *
     * @param id the id
     * @return the task by id
     */
    Task getTaskById(Long id);

    /**
     * Gets page sort tasks.
     *
     * @param searchDTO the search dto
     * @see com.webapplication.task_management_system.controller.TaskController
     * @return the page sort tasks
     */
    Page<Task> getPageSortTasks(SearchDTO searchDTO);
}
