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
}
