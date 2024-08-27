package com.webapplication.task_management_system.services.impl;

import com.webapplication.task_management_system.entity.task.Comment;
import com.webapplication.task_management_system.repository.CommentRepository;
import com.webapplication.task_management_system.services.CommentService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment getComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> {
            final String message = STR."Comment with id=\{id} is not found!";
            log.warn(message);
            return new IllegalArgumentException(message);
        });
    }

    @Transactional
    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Page<Comment> getPageSortTasks(Pageable pageable, String authorEmail, Long taskId) {

        if(!StringUtils.isBlank(authorEmail) && !Objects.isNull(taskId))
            return commentRepository.findAll(pageable, authorEmail, taskId);

        if (!Objects.isNull(taskId))
            return commentRepository.findAllByTaskId(pageable, taskId);

        if (!StringUtils.isBlank(authorEmail))
            return commentRepository.findAllByAuthorEmail(pageable, authorEmail);

        return commentRepository.findAll(pageable);
    }
}
