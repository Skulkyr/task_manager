package com.webapplication.task_management_system.entity.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@RequiredArgsConstructor
@Getter
public enum Priority {
    LOW("low"),
    MIDDLE("middle"),
    HIGH("high");

    private final String description;

    public static Priority fromString(String value) {
        for (Priority priority : Priority.values()) {
            if (priority.description.equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Priority with this name is not found");
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("description", description)
                .toString();
    }
}
