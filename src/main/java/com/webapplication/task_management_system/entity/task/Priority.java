package com.webapplication.task_management_system.entity.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Priority {
    low("низкий"),
    middle("средний"),
    high("высокий");

    private final String description;

    public static Priority fromString(String value) {
        for (Priority priority : Priority.values()) {
            if (priority.description.equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Priority with this name is not found");
    }
}
