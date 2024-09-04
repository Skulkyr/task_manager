package com.webapplication.task_management_system.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapplication.task_management_system.DTO.comment.CommentRequest;
import com.webapplication.task_management_system.DTO.comment.CommentResponse;
import com.webapplication.task_management_system.entity.task.Comment;
import com.webapplication.task_management_system.entity.task.Task;
import com.webapplication.task_management_system.mapper.CommentMapper;
import com.webapplication.task_management_system.services.CommentService;
import com.webapplication.task_management_system.services.TaskService;
import com.webapplication.task_management_system.utils.ValidUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentService commentService;
    @Mock
    private TaskService taskService;
    @Mock
    @SuppressWarnings("all")
    private ValidUtils validUtils;
    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private CommentController commentController;
    private MockMvc mvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Task task;
    private Comment comment;
    private CommentRequest commentRequest;
    private CommentResponse commentResponse;
    private final long ID = 1L;


    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(commentController).build();
        task = new Task();
        task.setId(ID);
        comment = new Comment();
        comment.setId(ID);
        comment.setComment("testing");
        commentRequest = new CommentRequest();
        commentRequest.setComment("testing");
        commentResponse = CommentResponse.builder().id(ID).comment("testing").build();
    }


    /*@Test
    @SneakyThrows
    void getComments() {
        List<Comment> comments = new ArrayList<>();
        List<CommentResponse> commentResponseList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Comment comment = new Comment();
            comment.setId((long) i);
            comment.setComment(STR."test \{i}");
            comments.add(comment);
            CommentResponse commentResponse = CommentResponse.builder().id((long) i).comment(STR."test \{i}").build();
            commentResponseList.add(commentResponse);
            when(commentMapper.commentToCommentResponse(comment)).thenReturn(commentResponse);
        }

        when(commentService.getPageSortTasks(any(Pageable.class), any(String.class), any(Long.class)))
                .thenReturn(new PageImpl<>(comments));
        String expected = objectMapper.writeValueAsString(commentResponseList);


        mvc.perform(get("/task/2/comments?authorEmail=test"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));


        verify(commentService, times(1)).getPageSortTasks(any(Pageable.class), any(String.class), any(Long.class));
        verify(commentMapper, times(10)).commentToCommentResponse(any(Comment.class));
    }*/

    @Test
    @SneakyThrows
    void addComment_ifSendValidData_WhenReturn201() {
        when(commentMapper.commentRequestToComment(commentRequest)).thenReturn(comment);
        when(taskService.getTaskById(ID)).thenReturn(task);
        when(commentService.addComment(comment)).thenReturn(comment);
        when(commentMapper.commentToCommentResponse(comment)).thenReturn(commentResponse);


        mvc.perform(post("/task/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().is(201))
                .andExpect(content().json(objectMapper.writeValueAsString(commentResponse)));


        verify(commentMapper, times(1)).commentRequestToComment(commentRequest);
        verify(taskService, times(1)).getTaskById(task.getId());
        verify(commentService, times(1)).addComment(comment);
        verify(commentMapper, times(1)).commentToCommentResponse(comment);
    }
    @ParameterizedTest
    @ValueSource(strings = {"test", "123"})
    @NullAndEmptySource
    @SneakyThrows
    void addComment_ifSendNotValidData_WhenReturn400(String comment) {
        commentRequest.setComment(comment);


        mvc.perform(post("/task/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                        .andExpect(status().is(400));


        verify(commentMapper, never()).commentRequestToComment(any());
        verify(taskService, never()).getTaskById(any());
        verify(commentService, never()).addComment(any());
        verify(commentMapper, never()).commentToCommentResponse(any());
    }

    @Test
    @SneakyThrows
    void deleteComment() {
        mvc.perform(delete("/task/*/comments/1"))
                        .andExpect(status().isOk());


        verify(commentService, times(1)).getComment(ID);
        verify(commentService, times(1)).deleteComment(ID);
    }

    @Test
    @SneakyThrows
    void putCommit_ifSendValidData_ThenReturn200() {
        when(commentService.getComment(ID)).thenReturn(comment);
        when(commentMapper.commentToCommentResponse(comment)).thenReturn(commentResponse);
        when(commentService.addComment(comment)).thenReturn(comment);

        mvc.perform(put("/task/*/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentResponse)));


        verify(commentService, times(1)).getComment(ID);
        verify(commentMapper, times(1)).commentToCommentResponse(comment);
        verify(commentService, times(1)).addComment(comment);
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "123"})
    @NullAndEmptySource
    @SneakyThrows
    void putCommit_ifSendNotValidData_ThenReturn400(String comment) {
        commentRequest.setComment(comment);

        mvc.perform(put("/task/*/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isBadRequest());


        verify(commentService, never()).getComment(ID);
        verify(commentMapper, never()).commentToCommentResponse(any());
        verify(commentService, never()).addComment(any());
    }

}

