package com.webapplication.task_management_system.utils;

import com.webapplication.task_management_system.entity.task.Comment;
import com.webapplication.task_management_system.entity.task.Task;
import com.webapplication.task_management_system.entity.user.Role;
import com.webapplication.task_management_system.entity.user.User;
import com.webapplication.task_management_system.exception.TaskEditPermissionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ValidUtils {

    public void checkErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new IllegalArgumentException(bindingResultErrorToString(bindingResult));
    }

    public void checkEditTaskAuthorityAndFill(Task taskFromDb, Task task, User user) {

        task.setId(taskFromDb.getId());

        if (taskFromDb.getAuthor().equals(user))
            task.setAuthor(user);

         else if (!user.getRoles().contains(Role.ROLE_ADMIN))
            throw new TaskEditPermissionException("You cant edit this task!");
    }

    public void checkEditTaskAuthority(Task taskFromDb, User user) {

        if (Objects.isNull(user) ||
                        !(taskFromDb.getAuthor().equals(user) ||
                        user.getRoles().contains(Role.ROLE_ADMIN)))
            throw new TaskEditPermissionException("You cant edit this task!");
    }

    public void checkEditTaskStatusAuthority(Task taskFromDb, User user) {
        if (Objects.isNull(user) ||
                        !(taskFromDb.getAuthor().equals(user) ||
                        taskFromDb.getExecutor().equals(user) ||
                        user.getRoles().contains(Role.ROLE_ADMIN)))
            throw new TaskEditPermissionException("You cant edit this task!");
    }

    public void checkEditCommentAuthority(Comment commentFromDb, User user) {

        if (Objects.isNull(user) ||
                !(commentFromDb.getAuthor().equals(user) ||
                        user.getRoles().contains(Role.ROLE_ADMIN)))
            throw new TaskEditPermissionException("You cant edit this comment!");
    }

    private String bindingResultErrorToString(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("  &  "));
    }
}
