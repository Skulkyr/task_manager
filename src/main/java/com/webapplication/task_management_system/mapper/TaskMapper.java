package com.webapplication.task_management_system.mapper;

import com.webapplication.task_management_system.DTO.task.TaskRequest;
import com.webapplication.task_management_system.DTO.task.TaskResponse;
import com.webapplication.task_management_system.entity.task.Task;
import com.webapplication.task_management_system.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = MapperUtils.class)
public interface TaskMapper {

    @Mapping(source = "author.email", target = "authorEmail")
    @Mapping(source = "executor.email", target = "executorEmail")
    @Mapping(source = "status.description", target = "status")
    @Mapping(source = "priority.description", target = "priority")
    @Mapping(source = "createDate", target = "createDate")
    TaskResponse taskToTaskResponse(Task task);

    @Mapping(source = "executorEmail", target = "executor", qualifiedByName = "setExecutor")
    @Mapping(source = "priority", target = "priority", qualifiedByName = "mapStringToPriority")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStringToStatus")
    Task taskRequestToTask(TaskRequest taskRequest) throws IllegalArgumentException;

}
