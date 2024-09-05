package com.webapplication.task_management_system.services;

import com.webapplication.task_management_system.DTO.criteria.SearchDTO;
import com.webapplication.task_management_system.entity.task.Comment;
import org.springframework.data.domain.Page;

/**
 * The interface Comment service.
 */
public interface CommentService {

    /**
     * Method for add or update comment
     *
     * @param comment comment entity
     * @return comment with id after save or update
     */
    Comment addComment(Comment comment);

    /**
     * Get comment entity by id
     *
     * @param id the id
     * @return the comment
     */
    Comment getComment(Long id);

    /**
     * Delete comment.
     *
     * @param id the id
     */
    void deleteComment(Long id);

    /**
     * Gets page of comment.
     *
     * @param searchDTO the these are DTOs containing search criteria and operators.
     */
    Page<Comment> getPageSortTasks(SearchDTO searchDTO);
}
