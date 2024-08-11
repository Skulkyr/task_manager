package com.webapplication.task_management_system.entity.task;

import com.webapplication.task_management_system.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    @ManyToOne()
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    @CreationTimestamp
    private LocalDateTime createDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Comment comment1 = (Comment) o;

        return new EqualsBuilder().append(id, comment1.id).append(comment, comment1.comment).append(author, comment1.author).append(createDate, comment1.createDate).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(comment).append(author).append(createDate).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("comment", comment)
                .append("author", author)
                .append("taskId", task.getId())
                .append("createDate", createDate)
                .toString();
    }
}
