package com.webapplication.task_management_system.controller;

import com.webapplication.task_management_system.DTO.comment.CommentRequest;
import com.webapplication.task_management_system.DTO.comment.CommentResponse;
import com.webapplication.task_management_system.entity.task.Comment;
import com.webapplication.task_management_system.entity.user.User;
import com.webapplication.task_management_system.mapper.CommentMapper;
import com.webapplication.task_management_system.services.CommentService;
import com.webapplication.task_management_system.services.TaskService;
import com.webapplication.task_management_system.utils.ValidUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class CommentController {
    private final TaskService taskService;
    private final ValidUtils validUtils;
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping( value = {"/comments", "/{taskId}/comments"})
    public List<CommentResponse> getComments(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "createDate") String sortBy,
                                             @RequestParam(defaultValue = "DESC") Sort.Direction direction,
                                             @RequestParam(required = false) String authorEmail,
                                             @PathVariable(required = false) Long taskId) {

        Pageable pageable = PageRequest.of(page, size, direction, sortBy);

        return commentService.getPageSortTasks(pageable, authorEmail, taskId).stream()
                .map(commentMapper::commentToCommentResponse)
                .toList();
    }

    @PostMapping("/{commentId}/comments")
    public ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest commentRequest,
                                                      @AuthenticationPrincipal User user,
                                                      @PathVariable("commentId") Long commentId,
                                                      BindingResult bindingResult) {
        validUtils.checkErrors(user, bindingResult);

        Comment comment = commentMapper.commentRequestToComment(commentRequest);
        comment.setTask(taskService.getTaskById(commentId));
        comment.setAuthor(user);
        comment = commentService.addComment(comment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentMapper.commentToCommentResponse(comment));
    }

    @DeleteMapping("/*/comments/{commentId}")
    public void deleteComment(@AuthenticationPrincipal User user,
                              @PathVariable("commentId") Long commentId) {

        Comment commentFromDb = commentService.getComment(commentId);
        validUtils.checkEditCommentAuthority(commentFromDb, user);
        commentService.deleteComment(commentId);
    }

    @PutMapping("/*/comments/{commentId}")
    public CommentResponse editComment(@AuthenticationPrincipal User user,
                                       @PathVariable("commentId") Long commentId,
                                       @Valid @RequestBody CommentRequest commentRequest
                                       ) {

        Comment commentFromDb = commentService.getComment(commentId);
        validUtils.checkEditCommentAuthority(commentFromDb, user);
        commentFromDb.setComment(commentRequest.getComment());
        return commentMapper.commentToCommentResponse(commentService.addComment(commentFromDb));
    }
}
