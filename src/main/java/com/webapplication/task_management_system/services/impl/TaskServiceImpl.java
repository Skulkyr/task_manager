package com.webapplication.task_management_system.services.impl;

import com.webapplication.task_management_system.DTO.criteria.SearchDTO;
import com.webapplication.task_management_system.entity.task.Task;
import com.webapplication.task_management_system.repository.TaskRepository;
import com.webapplication.task_management_system.services.SpecificationService;
import com.webapplication.task_management_system.services.TaskService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final SpecificationService<Task> specificationService;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTaskFromId(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Task getTaskById(Long id) {return taskRepository.findById(id).orElseThrow(() -> {
        final String message = STR."Task with id=\{id} is not found!";
        log.warn(message);
        return new IllegalArgumentException(message);
    });
    }

    @Transactional
    @Override
    public Page<Task> getPageSortTasks(SearchDTO searchDTO) {
        var specification = specificationService.getSearchSpecifications(searchDTO);

        return taskRepository.findAll(specification, PageRequest.of(0, 10, Sort.Direction.ASC, "id"));
    }
}
