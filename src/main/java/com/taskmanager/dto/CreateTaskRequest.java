
package com.taskmanager.dto;

import com.taskmanager.entity.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateTaskRequest(

        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100,
                message = "Title must contain between 3 and 100 characters")
        String title,

        @Size(max = 500,
                message = "Description cannot exceed 500 characters")
        String description,

        TaskStatus status,

        @FutureOrPresent(message = "Due date cannot be in the past")
        LocalDate dueDate
) {
}
