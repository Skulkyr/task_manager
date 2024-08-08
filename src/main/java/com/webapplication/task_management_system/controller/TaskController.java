package com.webapplication.task_management_system.controller;

import com.webapplication.task_management_system.DTO.task.TaskRequest;
import com.webapplication.task_management_system.DTO.task.TaskResponse;
import com.webapplication.task_management_system.DTO.task.TaskStatusRequest;
import com.webapplication.task_management_system.entity.task.Status;
import com.webapplication.task_management_system.entity.task.Task;
import com.webapplication.task_management_system.entity.user.User;
import com.webapplication.task_management_system.mapper.TaskMapper;
import com.webapplication.task_management_system.services.TaskService;
import com.webapplication.task_management_system.utils.ValidUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private final TaskMapper taskMapper;
    private final TaskService taskService;
    private final ValidUtils validUtils;

    @PostMapping
    public ResponseEntity<TaskResponse> saveTask(@Valid @RequestBody TaskRequest taskRequest,
                                                 BindingResult bindingResult,
                                                 @AuthenticationPrincipal User user) {

        validUtils.checkErrors(user, bindingResult);

        Task task = taskMapper.taskRequestToTask(taskRequest);
        task.setAuthor(user);
        task = taskService.saveTask(task);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskMapper.taskToTaskResponse(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> editTask(@Valid @RequestBody TaskRequest taskRequest,
                                                 BindingResult bindingResult,
                                                 @AuthenticationPrincipal User user,
                                                 @PathVariable("id") Long id) {

        validUtils.checkErrors(user, bindingResult);

        Task taskFromDb = taskService.getTaskById(id);
        Task task = taskMapper.taskRequestToTask(taskRequest);

        validUtils.checkEditTaskAuthorityAndFill(taskFromDb, task, user);

        task = taskService.saveTask(task);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskMapper.taskToTaskResponse(task));
    }

    @PutMapping("/{id}/status")
    public void changeStatus(@Valid @RequestBody TaskStatusRequest taskStatusRequest,
                             BindingResult bindingResult,
                             @AuthenticationPrincipal User user,
                             @PathVariable("id") Long id) {

        validUtils.checkErrors(user, bindingResult);
        Task taskFromDb = taskService.getTaskById(id);
        validUtils.checkEditTaskStatusAuthority(taskFromDb, user);

        Status status = Status.fromString(taskStatusRequest.getStatus());
        taskFromDb.setStatus(status);
        taskService.saveTask(taskFromDb);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@AuthenticationPrincipal User user,
                           @PathVariable("id") Long id) {

        Task taskFromDb = taskService.getTaskById(id);
        validUtils.checkEditTaskAuthority(taskFromDb, user);
        taskService.deleteTaskFromId(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskMapper.taskToTaskResponse(taskService.getTaskById(id)));
    }

    @GetMapping()
    public List<TaskResponse> getSortedTask(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "createDate") String sortBy,
                                            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
                                            @RequestParam(required = false) String authorEmail,
                                            @RequestParam(required = false) String executorEmail) {

        Pageable pageable = PageRequest.of(page, size, direction, sortBy);

        return taskService
                .getPageSortTasks(pageable, authorEmail, executorEmail).stream()
                .map(taskMapper::taskToTaskResponse)
                .toList();
    }
}
