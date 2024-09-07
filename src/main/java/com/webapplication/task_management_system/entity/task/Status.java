package com.webapplication.task_management_system.entity.task;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

@RequiredArgsConstructor
@Getter
public enum Status {
    WAITING("waiting"),
    IN_PROCESS("in process"),
    DONE("done");

    private final String description;

    public static Status fromString(String value) throws IllegalArgumentException {
        for (Status status : Status.values()) {
            if (status.description.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status with this name is not found");
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("description", description)
                .toString();
    }
}
