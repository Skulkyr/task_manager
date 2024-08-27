package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.entity.task.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Comment addComment(Comment comment);
    Comment getComment(Long id);
    void deleteComment(Long id);
    Page<Comment> getPageSortTasks(Pageable pageable, String authorEmail, Long taskId);
}
