package com.webapplication.task_management_system.utils;

import com.webapplication.task_management_system.entity.task.Priority;
import com.webapplication.task_management_system.entity.task.Status;
import com.webapplication.task_management_system.entity.user.User;
import com.webapplication.task_management_system.services.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperUtils {
    private final UserService userService;

    @Named("setExecutor")
    public User getUserByEmail(String email) throws IllegalArgumentException {
        return email == null ? null : userService.getByEmail(email);
    }

    @Named("mapStringToPriority")
    public Priority mapStringToPriority(String priorityDescription) throws IllegalArgumentException {
        if (priorityDescription == null) return Priority.middle;
        return Priority.fromString(priorityDescription);
    }

    @Named("mapStringToStatus")
    public Status mapStringToStatus(String statusDescription) throws IllegalArgumentException {
        if (statusDescription == null) return Status.Waiting;
        return Status.fromString(statusDescription);
    }
}
