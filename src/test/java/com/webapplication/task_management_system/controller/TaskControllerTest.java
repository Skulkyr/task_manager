package com.webapplication.task_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapplication.task_management_system.DTO.task.TaskRequest;
import com.webapplication.task_management_system.DTO.task.TaskResponse;
import com.webapplication.task_management_system.DTO.task.TaskStatusRequest;
import com.webapplication.task_management_system.controller.advice.ExceptionHandler;
import com.webapplication.task_management_system.entity.task.Task;
import com.webapplication.task_management_system.entity.user.Role;
import com.webapplication.task_management_system.entity.user.User;
import com.webapplication.task_management_system.mapper.TaskMapper;
import com.webapplication.task_management_system.security.TestSecurityConfig;
import com.webapplication.task_management_system.services.TaskService;
import com.webapplication.task_management_system.utils.ValidUtils;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class)
@ActiveProfiles("test")
@Import({ValidUtils.class, ExceptionHandler.class, TestSecurityConfig.class})
public class TaskControllerTest {

    @MockBean
    private TaskMapper taskMapper;
    @MockBean
    private TaskService taskService;



    @Autowired
    private MockMvc mvc;
    private Task task;
    private TaskRequest taskRequest;
    private TaskResponse taskResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();


    final long ID = 1L;


    @BeforeEach
    void setUp() {
        User user = new User();
        String email = "test@mail.ru";
        user.setEmail(email);
        user.setRoles(List.of(Role.ROLE_USER));

        task = new Task();
        task.setId(ID);
        task.setDescription(".................................................");
        task.setTitle("...................");
        task.setAuthor(user);

        taskRequest = TaskRequest.builder()
                .description(".................................................")
                .title("...................")
                .build();

        taskResponse = TaskResponse.builder()
                .id(ID)
                .description(".................................................")
                .title("...................")
                .authorEmail(email)
                .build();
    }

    @Test
    @WithMockCustomUser
    @SneakyThrows
    void saveTask_ifSendDataIsValid_ThenReturn201() {

        // Arrange: Preparation
        when(taskMapper.taskRequestToTask(taskRequest)).thenReturn(task);
        when(taskService.saveTask(task)).thenReturn(task);
        when(taskMapper.taskToTaskResponse(task)).thenReturn(taskResponse);

        // Act: Execution
        mvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))

                // Assert: Verification of results
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(taskResponse)));

        // Assert: Verification of interactions
        verify(taskMapper, times(1)).taskRequestToTask(taskRequest);
        verify(taskService, times(1)).saveTask(task);
        verify(taskMapper, times(1)).taskToTaskResponse(task);
    }

    @Test
    @WithMockCustomUser
    @SneakyThrows
    void saveTask_ifSendDataIsInvalid_ThenReturn400() {

        // Arrange: Preparation
        taskRequest.setTitle("..");
        taskRequest.setDescription("..");
        taskRequest.setExecutorEmail("..");

        // Act: Execution
        mvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))

                // Assert: Verification of results
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Matchers.containsString("The title must contain from 6 to 40 characters")))
                .andExpect(jsonPath("$.message").value(Matchers.containsString("must be a well-formed email address")))
                .andExpect(jsonPath("$.message").value(Matchers.containsString("The description must contain from 40 to 2000 characters")));

        // Assert: Verification of interactions
        verify(taskMapper, never()).taskRequestToTask(taskRequest);
        verify(taskService, never()).saveTask(task);
        verify(taskMapper, never()).taskToTaskResponse(task);
    }

    @Test
    @SneakyThrows
    void saveTask_ifUnauthorized_ThenReturn400() {

        // Act: Execution
        mvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))


                // Assert: Verification of results
                .andExpect(status().isForbidden());


        // Assert: Verification of interactions
        verify(taskMapper, never()).taskRequestToTask(taskRequest);
        verify(taskService, never()).saveTask(task);
        verify(taskMapper, never()).taskToTaskResponse(task);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void editTask_ifDataIsValidAndSendAuthor_thenReturn200() {

        // Arrange: Preparation
        when(taskService.getTaskById(ID)).thenReturn(task);
        when(taskMapper.taskRequestToTask(taskRequest)).thenReturn(task);
        when(taskService.saveTask(task)).thenReturn(task);
        when(taskMapper.taskToTaskResponse(task)).thenReturn(taskResponse);


        // Act: Execution
        mvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))

                // Assert: Verification of results
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskResponse)));


        // Assert: Verification of interactions
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskMapper, times(1)).taskRequestToTask(taskRequest);
        verify(taskService, times(1)).saveTask(task);
        verify(taskMapper, times(1)).taskToTaskResponse(task);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser
    void editTask_ifDataIsInvalid_thenReturn400() {
        // Arrange: Preparation
        taskRequest.setTitle("..");
        taskRequest.setDescription("..");
        taskRequest.setExecutorEmail("..");

        // Act: Execution
        mvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                
                
                // Assert: Verification of results
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Matchers.containsString("The title must contain from 6 to 40 characters")))
                .andExpect(jsonPath("$.message").value(Matchers.containsString("must be a well-formed email address")))
                .andExpect(jsonPath("$.message").value(Matchers.containsString("The description must contain from 40 to 2000 characters")));


        // Assert: Verification of interactions
        verify(taskService, never()).getTaskById(ID);
        verify(taskMapper, never()).taskRequestToTask(taskRequest);
        verify(taskService, never()).saveTask(task);
        verify(taskMapper, never()).taskToTaskResponse(task);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(roles = {"ADMIN"}, username = "admin@mail.ru")
    void editTask_ifDataIsValidAndSendAdmin_thenReturn200() {

        // Arrange: Preparation
        when(taskService.getTaskById(ID)).thenReturn(task);
        when(taskMapper.taskRequestToTask(taskRequest)).thenReturn(task);
        when(taskService.saveTask(task)).thenReturn(task);
        when(taskMapper.taskToTaskResponse(task)).thenReturn(taskResponse);


        // Act: Execution
        mvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))

                // Assert: Verification of results
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskResponse)));


        // Assert: Verification of interactions
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskMapper, times(1)).taskRequestToTask(taskRequest);
        verify(taskService, times(1)).saveTask(task);
        verify(taskMapper, times(1)).taskToTaskResponse(task);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(username = "notAuthor@mail.ru")
    void editTask_ifDataIsInvalidAndSendNotAuthorOrAdmin_thenReturn400() {
        
        // Arrange: Preparation
        when(taskService.getTaskById(ID)).thenReturn(task);
        when(taskMapper.taskRequestToTask(taskRequest)).thenReturn(task);


        // Act: Execution
        mvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))

                // Assert: Verification of results
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You cant edit this task!"));


        // Assert: Verification of interactions
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskMapper, times(1)).taskRequestToTask(taskRequest);
        verify(taskService, never()).saveTask(task);
        verify(taskMapper, never()).taskToTaskResponse(task);
    }


    @Test
    @SneakyThrows
    void editTask_ifUnauthorized_thenReturn403() {
        // Arrange: Preparation
        taskRequest.setTitle("..");
        taskRequest.setDescription("..");
        taskRequest.setExecutorEmail("..");

        // Act: Execution
        mvc.perform(put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))


                // Assert: Verification of results
                .andExpect(status().isForbidden());


        // Assert: Verification of interactions
        verify(taskService, never()).getTaskById(ID);
        verify(taskMapper, never()).taskRequestToTask(taskRequest);
        verify(taskService, never()).saveTask(task);
        verify(taskMapper, never()).taskToTaskResponse(task);
    }


    @Test
    @SneakyThrows
    @WithMockCustomUser
    void changeStatus_ifDataIsValidAndSendAuthor_thenReturn200() {
        // Arrange: Preparation
        String statusStr = "в работе";
        TaskStatusRequest taskStatusRequest = new TaskStatusRequest();
        taskStatusRequest.setStatus(statusStr);
        when(taskService.getTaskById(ID)).thenReturn(task);
        when(taskService.saveTask(task)).thenReturn(task);

        // Act: Execution
        mvc.perform(put("/task/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusRequest)))

                // Assert: Verification of result
                .andExpect(status().isOk());


        //Assert: Verification of result
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskService, times(1)).saveTask(task);
    }


    @Test
    @SneakyThrows
    @WithMockCustomUser
    void changeStatus_ifDataIsInvalid_thenReturn400() {
        // Arrange: Preparation
        String statusStr = "ошибка";
        TaskStatusRequest taskStatusRequest = new TaskStatusRequest();
        taskStatusRequest.setStatus(statusStr);
        when(taskService.getTaskById(ID)).thenReturn(task);


        // Act: Execution
        mvc.perform(put("/task/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusRequest)))

                // Assert: Verification of result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Status with this name is not found"));


        //Assert: Verification of result
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskService, never()).saveTask(task);
    }

    @Test
    @SneakyThrows
    void changeStatus_ifUnauthorized_thenReturn403() {
        // Arrange: Preparation
        TaskStatusRequest taskStatusRequest = new TaskStatusRequest();

        // Act: Execution
        mvc.perform(put("/task/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusRequest)))

                // Assert: Verification of result
                .andExpect(status().isForbidden());


        //Assert: Verification of result
        verify(taskService, never()).getTaskById(ID);
        verify(taskService, never()).saveTask(task);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(username = "admin@mail.ru", roles = {"ADMIN"})
    void changeStatus_ifDataIsValidAndSendAdmin_thenReturn200() {
        // Arrange: Preparation
        String statusStr = "в работе";
        TaskStatusRequest taskStatusRequest = new TaskStatusRequest();
        taskStatusRequest.setStatus(statusStr);
        User executor = new User(null, null, null, "executor@mail.ru", null, List.of(Role.ROLE_USER), null, null, null, null);
        task.setExecutor(executor);
        when(taskService.getTaskById(ID)).thenReturn(task);
        when(taskService.saveTask(task)).thenReturn(task);

        // Act: Execution
        mvc.perform(put("/task/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusRequest)))

                // Assert: Verification of result
                .andExpect(status().isOk());


        //Assert: Verification of result
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskService, times(1)).saveTask(task);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(username = "executor@mail.ru")
    void changeStatus_ifDataIsValidAndSendExecutor_thenReturn200() {
        // Arrange: Preparation
        String statusStr = "в работе";
        TaskStatusRequest taskStatusRequest = new TaskStatusRequest();
        taskStatusRequest.setStatus(statusStr);
        User executor = new User(null, null, null, "executor@mail.ru", null, List.of(Role.ROLE_USER), null, null, null, null);
        task.setExecutor(executor);
        when(taskService.getTaskById(ID)).thenReturn(task);
        when(taskService.saveTask(task)).thenReturn(task);

        // Act: Execution
        mvc.perform(put("/task/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusRequest)))

                // Assert: Verification of result
                .andExpect(status().isOk());


        //Assert: Verification of result
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskService, times(1)).saveTask(task);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(username = "notRules@mail.ru")
    void changeStatus_ifDataIsValidAndSendNotAdminOrAuthorOrExecutor_thenReturn400() {
        // Arrange: Preparation
        String statusStr = "в работе";
        TaskStatusRequest taskStatusRequest = new TaskStatusRequest();
        taskStatusRequest.setStatus(statusStr);
        User executor = new User(null, null, null, "executor@mail.ru", null, List.of(Role.ROLE_USER), null, null, null, null);
        task.setExecutor(executor);
        when(taskService.getTaskById(ID)).thenReturn(task);

        // Act: Execution
        mvc.perform(put("/task/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskStatusRequest)))

                // Assert: Verification of result
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You cant edit this task!"));


        //Assert: Verification of result
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskService, never()).saveTask(task);
    }



    @Test
    @SneakyThrows
    @WithMockCustomUser
    void deleteTask_ifUserIsAuthor_thenReturn200() {
        // Arrange: Preparation
        when(taskService.getTaskById(ID)).thenReturn(task);

        // Act: Execution
        mvc.perform(delete("/task/1"))

                // Assert: Verification of result
                .andExpect(status().isOk());


        // Assert: Verification of result
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskService, times(1)).deleteTaskFromId(ID);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(username = "admin@mail.ru", roles = "ADMIN")
    void deleteTask_ifUserIsAdmin_thenReturn200() {
        // Arrange: Preparation
        when(taskService.getTaskById(ID)).thenReturn(task);

        // Act: Execution
        mvc.perform(delete("/task/1"))

                // Assert: Verification of result
                .andExpect(status().isOk());


        // Assert: Verification of result
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskService, times(1)).deleteTaskFromId(ID);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(username = "notRules@mail.ru")
    void deleteTask_ifUserIsNotAuthorOrAdmin_thenReturn403() {
        // Arrange: Preparation
        when(taskService.getTaskById(ID)).thenReturn(task);

        // Act: Execution
        mvc.perform(delete("/task/1"))

                // Assert: Verification of result
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You cant edit this task!"));


        // Assert: Verification of result
        verify(taskService, times(1)).getTaskById(ID);
        verify(taskService, never()).deleteTaskFromId(ID);
    }

    @Test
    @SneakyThrows
    void deleteTask_ifUnauthorized_thenReturn403() {
        
        // Act: Execution
        mvc.perform(delete("/task/1"))

                // Assert: Verification of result
                .andExpect(status().isForbidden());


        // Assert: Verification of result
        verify(taskService, never()).getTaskById(ID);
        verify(taskService, never()).deleteTaskFromId(ID);
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


    /*@Test
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
    }*/


}
