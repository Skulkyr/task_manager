package com.webapplication.task_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapplication.task_management_system.DTO.task.TaskRequest;
import com.webapplication.task_management_system.DTO.task.TaskResponse;
import com.webapplication.task_management_system.DTO.task.TaskStatusRequest;
import com.webapplication.task_management_system.entity.task.Task;
import com.webapplication.task_management_system.entity.user.User;
import com.webapplication.task_management_system.mapper.TaskMapper;
import com.webapplication.task_management_system.services.TaskService;
import com.webapplication.task_management_system.utils.ValidUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskMapper taskMapper;
    @Mock
    private TaskService taskService;
    @Mock
    private ValidUtils validUtils;
    @InjectMocks
    private TaskController taskController;
    private MockMvc mvc;
    private Task task;
    private TaskRequest taskRequest;
    private TaskResponse taskResponse;
    private final ObjectMapper objectMapper = new ObjectMapper();

    final long ID = 1L;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(taskController).build();
        task = new Task();
        task.setId(ID);
        task.setDescription(".................................................");
        task.setTitle("...................");
        taskRequest = TaskRequest.builder()
                .description(".................................................")
                .title("...................")
                .build();
        taskResponse = TaskResponse.builder()
                .id(ID)
                .description(".................................................")
                .title("...................")
                .build();
    }

    @Test
    @SneakyThrows
    void saveTask_ifSendValidData_ThenReturn201() {
        when(taskMapper.taskRequestToTask(taskRequest)).thenReturn(task);
        when(taskService.saveTask(task)).thenReturn(task);
        when(taskMapper.taskToTaskResponse(task)).thenReturn(taskResponse);


        mvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(taskResponse)));


        verify(validUtils, times(1)).checkErrors(any(User.class), any(BindingResult.class));
        verify(taskMapper, times(1)).taskRequestToTask(taskRequest);
        verify(taskService, times(1)).saveTask(task);
        verify(taskMapper, times(1)).taskToTaskResponse(task);
    }

    @Test
    @SneakyThrows
    void editTask() {
        when(taskService.getTaskById(ID)).thenReturn(task);
        when(taskMapper.taskRequestToTask(taskRequest)).thenReturn(task);
        when(taskService.saveTask(task)).thenReturn(task);
        when(taskMapper.taskToTaskResponse(task)).thenReturn(taskResponse);


        mvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                        .andExpect(status().isOk())
                        .andExpect(content().json(objectMapper.writeValueAsString(taskResponse)));


        verify(validUtils, times(1)).checkErrors(any(User.class), any(BindingResult.class));
        verify(validUtils, times(1)).checkEditTaskAuthorityAndFill(any(Task.class), any(Task.class), any(User.class));
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskMapper, times(1)).taskRequestToTask(taskRequest);
        verify(taskService, times(1)).saveTask(task);
        verify(taskMapper, times(1)).taskToTaskResponse(task);
    }

    @Test
    @SneakyThrows
    void changeStatus() {
        String statusStr = "в работе";
        TaskStatusRequest taskStatusRequest = new TaskStatusRequest();
        taskStatusRequest.setStatus(statusStr);
        when(taskService.getTaskById(ID)).thenReturn(task);
        when(taskService.saveTask(task)).thenReturn(task);


        mvc.perform(put("/task/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskStatusRequest)))
                .andExpect(status().isOk());


        verify(validUtils, times(1)).checkErrors(any(User.class), any(BindingResult.class));
        verify(taskService, times(1)).getTaskById(ID);
        verify(validUtils, times(1)).checkEditTaskStatusAuthority(any(Task.class), any(User.class));
        verify(taskService, times(1)).saveTask(task);
    }

    @Test
    @SneakyThrows
    void deleteTask() {
        when(taskService.getTaskById(ID)).thenReturn(task);


        mvc.perform(delete("/task/1"))
                        .andExpect(status().isOk());


        verify(validUtils, times(1)).checkEditTaskAuthority(any(Task.class), any(User.class));
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskService, times(1)).deleteTaskFromId(ID);
    }

    @Test
    @SneakyThrows
    void getTask() {
        when(taskService.getTaskById(ID)).thenReturn(task);
        when(taskMapper.taskToTaskResponse(task)).thenReturn(taskResponse);


        mvc.perform(get("/task/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskResponse)));


        verify(taskService, times(1)).getTaskById(ID);
        verify(taskMapper, times(1)).taskToTaskResponse(task);
    }


    @Test
    @SneakyThrows
    void getSortedTask() {
        List<Task> tasks = new ArrayList<>();
        List<TaskResponse> taskResponses = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Task tempTask = new Task();
            tempTask.setId((long) i);
            tempTask.setTitle(task.getTitle()+i);
            tempTask.setDescription(task.getDescription()+i);
            TaskResponse tempResponse = TaskResponse.builder()
                    .id((long) i)
                    .title(taskResponse.getTitle()+i)
                    .description(taskResponse.getDescription()+i)
                    .build();
            tasks.add(tempTask);
            taskResponses.add(tempResponse);
            when(taskMapper.taskToTaskResponse(tempTask)).thenReturn(tempResponse);
        }

        when(taskService.getPageSortTasks(any(Pageable.class), any(), any())).thenReturn(new PageImpl<>(tasks));



        mvc.perform(get("/task"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskResponses)));


        verify(taskService, times(1)).getPageSortTasks(any(Pageable.class), any(), any());
        verify(taskMapper, times(10)).taskToTaskResponse(any(Task.class));
    }


}
