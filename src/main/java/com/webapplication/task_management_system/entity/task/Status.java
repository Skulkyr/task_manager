package com.webapplication.task_management_system.entity.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
    Waiting("в ожидании"),
    In_process("в работе"),
    Done("завершено");

    private final String description;

    public static Status fromString(String value) throws IllegalArgumentException {
        for (Status status : Status.values()) {
            if (status.description.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status with this name is not found");
    }
}
