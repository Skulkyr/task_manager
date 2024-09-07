package com.webapplication.task_management_system.controller;

import com.webapplication.task_management_system.DTO.comment.CommentRequest;
import com.webapplication.task_management_system.DTO.comment.CommentResponse;
import com.webapplication.task_management_system.DTO.criteria.SearchDTO;
import com.webapplication.task_management_system.entity.task.Comment;
import com.webapplication.task_management_system.entity.user.User;
import com.webapplication.task_management_system.mapper.CommentMapper;
import com.webapplication.task_management_system.services.CommentService;
import com.webapplication.task_management_system.services.TaskService;
import com.webapplication.task_management_system.utils.ValidUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Comment controller", description = "Manages the creation, receipt and editing of comments")
@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class CommentController {
    private final TaskService taskService;
    private final ValidUtils validUtils;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    @Operation(summary = "Get a filtered and sorted list of comments", description = """
            Valid field values:
       
            id, comment, author
            
            It is also possible to search for adjacent entities; to do this,
            you need to specify the entity and the field separated by a dot, for example 'author.id'.
            List of available fields for 'author.' and 'task.author.' and 'task.executor.' :
            
            id, firstName, lastName, email
            
            List of available fields for 'task.' :
            
            id, title, description, status, priority.
            
            Available priority values: LOW, MIDDLE, HIGH
            Available status values: WAITING, IN_PROCESS, DONE
            """)
    @PostMapping( value = {"/comments/search"})
    public Page<CommentResponse> getComments(@RequestBody(required = false) SearchDTO searchDTO) {

        if (searchDTO == null) searchDTO = new SearchDTO();
        Page<Comment> commentPage = commentService.getPageSortTasks(searchDTO);

        return new PageImpl<>(
                commentPage.getContent().stream().map(commentMapper::commentToCommentResponse).toList(),
                commentPage.getPageable(),
                commentPage.getTotalElements());
    }
    @Operation(summary = "Add new comment")
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest commentRequest,
                                                      @AuthenticationPrincipal User user,
                                                      @PathVariable("taskId") Long taskId,
                                                      BindingResult bindingResult) {
        validUtils.checkErrors(bindingResult);

        Comment comment = commentMapper.commentRequestToComment(commentRequest);
        comment.setTask(taskService.getTaskById(taskId));
        comment.setAuthor(user);
        comment = commentService.addComment(comment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentMapper.commentToCommentResponse(comment));
    }

    @Operation(summary = "Delete comment", description = "Only the author or adin has the right to edit or delete comments")
    @DeleteMapping("/*/comments/{commentId}")
    public void deleteComment(@AuthenticationPrincipal User user,
                              @PathVariable("commentId") Long commentId) {

        Comment commentFromDb = commentService.getComment(commentId);
        validUtils.checkEditCommentAuthority(commentFromDb, user);
        commentService.deleteComment(commentId);
    }

    @Operation(summary = "Edit comment", description = "Only the author or adin has the right to edit or delete comments")
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
