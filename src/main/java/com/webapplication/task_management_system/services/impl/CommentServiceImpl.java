package com.webapplication.task_management_system.services.impl;

import com.webapplication.task_management_system.DTO.criteria.SearchDTO;
import com.webapplication.task_management_system.entity.task.Comment;
import com.webapplication.task_management_system.repository.CommentRepository;
import com.webapplication.task_management_system.services.CommentService;
import com.webapplication.task_management_system.services.SpecificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final SpecificationService<Comment> specificationService;

    @Override
    @Transactional
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public Page<Comment> getPageSortTasks(SearchDTO searchDTO) {

        var specification = specificationService.getSearchSpecifications(searchDTO);

        return commentRepository.findAll(specification, searchDTO.getPageable());
    }
}
