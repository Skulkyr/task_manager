package com.webapplication.task_management_system.entity.task;

import com.webapplication.task_management_system.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Enumerated(EnumType.ORDINAL)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;
}
