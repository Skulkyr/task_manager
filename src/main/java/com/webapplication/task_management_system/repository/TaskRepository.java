package com.webapplication.task_management_system.repository;

import com.webapplication.task_management_system.entity.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t from Task t where t.author.email = ?1")
    Page<Task> findAllByAuthorEmail(Pageable pageable, String authorEmail);

    @Query("select t from Task t where t.executor.email = ?1")
    Page<Task> findAllByExecutorEmail(Pageable pageable, String executorEmail);

    @Query("select t from Task t where t.author.email = ?1 and t.executor.email = ?2")
    Page<Task> findAll(Pageable pageable, String authorEmail, String executorEmail);
}
