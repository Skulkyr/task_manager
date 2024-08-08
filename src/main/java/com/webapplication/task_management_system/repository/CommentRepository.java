package com.webapplication.task_management_system.repository;

import com.webapplication.task_management_system.entity.task.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.author.email = ?1")
    Page<Comment> findAllByAuthorEmail(Pageable pageable, String authorEmail);

    @Query("select c from Comment c where c.author.email = ?1 and c.task.id = ?2")
    Page<Comment> findAll(Pageable pageable, String authorEmail, Long taskId);

    @Query("select c from Comment c where c.task.id = ?1")
    Page<Comment> findAllByTaskId(Pageable pageable, Long taskId);
}
