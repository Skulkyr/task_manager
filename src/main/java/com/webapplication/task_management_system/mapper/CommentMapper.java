package com.webapplication.task_management_system.mapper;

import com.webapplication.task_management_system.DTO.comment.CommentRequest;
import com.webapplication.task_management_system.DTO.comment.CommentResponse;
import com.webapplication.task_management_system.entity.task.Comment;
import com.webapplication.task_management_system.utils.MapperUtils;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = MapperUtils.class)
public interface CommentMapper {
    @Mapping(source = "author.email", target = "authorEmail")
    @Mapping(source = "task.id", target = "taskId")
    CommentResponse commentToCommentResponse(Comment comment);

    Comment commentRequestToComment(CommentRequest commentRequest);

}
