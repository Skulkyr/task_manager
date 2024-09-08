package com.webapplication.task_management_system.controller;

import com.webapplication.task_management_system.DTO.criteria.SearchDTO;
import com.webapplication.task_management_system.DTO.task.TaskRequest;
import com.webapplication.task_management_system.DTO.task.TaskResponse;
import com.webapplication.task_management_system.DTO.task.TaskStatusRequest;
import com.webapplication.task_management_system.entity.task.Status;
import com.webapplication.task_management_system.entity.task.Task;
import com.webapplication.task_management_system.entity.user.User;
import com.webapplication.task_management_system.mapper.TaskMapper;
import com.webapplication.task_management_system.services.TaskService;
import com.webapplication.task_management_system.utils.ValidUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Task controller", description = "Manages the creation, receipt and editing of tasks")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;
    private final ValidUtils validUtils;


    @Operation(summary = "Save task")
    @PostMapping
    public ResponseEntity<TaskResponse> saveTask(@Valid @RequestBody TaskRequest taskRequest,
                                                 BindingResult bindingResult,
                                                 @AuthenticationPrincipal User user) {

        validUtils.checkErrors(bindingResult);

        Task task = taskMapper.taskRequestToTask(taskRequest);
        task.setAuthor(user);
        task = taskService.saveTask(task);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskMapper.taskToTaskResponse(task));
    }

    @Operation(summary = "Edit task", description = "Only the creator or admin can edit a task")
    @PutMapping("/{id}")
    @Transactional
    @Retryable(retryFor = OptimisticLockException.class)
    public ResponseEntity<TaskResponse> editTask(@Valid @RequestBody TaskRequest taskRequest,
                                                 BindingResult bindingResult,
                                                 @AuthenticationPrincipal User user,
                                                 @PathVariable("id") @Parameter(description = "Task id") Long id) {

        validUtils.checkErrors(bindingResult);

        Task taskFromDb = taskService.getTaskById(id);
        Task task = taskMapper.taskRequestToTask(taskRequest);

        validUtils.checkEditTaskAuthorityAndFill(taskFromDb, task, user);

        task = taskService.saveTask(task);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskMapper.taskToTaskResponse(task));
    }

    @Operation(summary = "Change task status", description = "Only the creator or admin can edit a task")
    @PutMapping("/{id}/status")
    @Transactional
    @Retryable(retryFor = OptimisticLockException.class)
    public void changeStatus(@Valid @RequestBody TaskStatusRequest taskStatusRequest,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal User user,
                             @PathVariable("id") @Parameter(description = "Task id") Long id) {

        validUtils.checkErrors(bindingResult);
        Task taskFromDb = taskService.getTaskById(id);
        validUtils.checkEditTaskStatusAuthority(taskFromDb, user);

        Status status = Status.fromString(taskStatusRequest.getStatus());
        taskFromDb.setStatus(status);
        taskService.saveTask(taskFromDb);
    }
    @Operation(summary = "Delete task", description = "Only the creator or admin can edit a task")
    @DeleteMapping("/{id}")
    @Transactional
    @Retryable(retryFor = OptimisticLockException.class)
    public void deleteTask(@AuthenticationPrincipal User user,
                           @PathVariable("id") @Parameter(description = "Task id") Long id) {

        Task taskFromDb = taskService.getTaskById(id);
        validUtils.checkEditTaskAuthority(taskFromDb, user);
        taskService.deleteTaskFromId(id);
    }


    @Operation(summary = "Get task by id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable("id") @Parameter(description = "Task id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskMapper.taskToTaskResponse(taskService.getTaskById(id)));
    }

    @Operation(summary = "Get a filtered and sorted list of tasks", description = """
            Valid field values:
            
            id, title, description, status, priority.
            
            It is also possible to search for adjacent entities; to do this,
            you need to specify the entity and the field separated by a dot, for example 'author.id'.
            List of available fields for 'author.' and 'executor.' :
            
            id, firstName, lastName, email
            
            Available priority values: LOW, MIDDLE, HIGH
            Available status values: WAITING, IN_PROCESS, DONE
            """)

    @PostMapping("/search")
    public Page<TaskResponse> getSortedTask(@RequestBody(required = false) SearchDTO searchDTO) {

        if (searchDTO == null) searchDTO = new SearchDTO();
        Page<Task> taskPage = taskService.getPageSortTasks(searchDTO);

        return new PageImpl<>(
                taskPage.getContent().stream().map(taskMapper::taskToTaskResponse).toList(),
                taskPage.getPageable(),
                taskPage.getTotalElements());
    }
}
