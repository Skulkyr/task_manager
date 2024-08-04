package com.webapplication.task_management_system.mapper;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.stream.Collectors;

@Component
public class ErrorMapper {

    public String bindingResultErrorToString(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining("\n"));
    }
}
