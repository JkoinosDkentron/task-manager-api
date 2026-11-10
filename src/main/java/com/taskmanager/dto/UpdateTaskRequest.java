
package com.taskmanager.dto;

import com.taskmanager.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateTaskRequest(

        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100)
        String title,

        @Size(max = 500)
        String description,

        @NotNull(message = "Status is required")
        TaskStatus status,

        LocalDate dueDate
) {
}
