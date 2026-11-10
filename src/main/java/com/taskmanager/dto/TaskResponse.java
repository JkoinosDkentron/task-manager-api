
package com.taskmanager.dto;

import com.taskmanager.entity.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDate dueDate,
        LocalDateTime createdAt
) {
}
