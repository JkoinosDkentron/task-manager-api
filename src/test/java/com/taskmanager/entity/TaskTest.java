
package com.taskmanager.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldCreateTaskWithPendingStatusByDefault() {
        Task task = new Task(
                "Prepare technical interview",
                "Study Spring Boot",
                null,
                LocalDate.now().plusDays(1)
        );

        assertEquals(TaskStatus.PENDING, task.getStatus());
        assertNotNull(task.getCreatedAt());
    }

    @Test
    void shouldUpdateOnlyTaskStatus() {
        Task task = new Task(
                "Prepare technical interview",
                "Study Spring Boot",
                TaskStatus.PENDING,
                null
        );

        task.updateStatus(TaskStatus.COMPLETED);

        assertEquals(TaskStatus.COMPLETED, task.getStatus());
        assertEquals("Prepare technical interview", task.getTitle());
    }

    @Test
    void shouldUpdateTaskInformation() {
        Task task = new Task(
                "Prepare report",
                null,
                TaskStatus.PENDING,
                null
        );

        LocalDate newDueDate = LocalDate.now().plusDays(3);

        task.update(
                "Prepare weekly report",
                "Include project progress",
                TaskStatus.IN_PROGRESS,
                newDueDate
        );

        assertEquals("Prepare weekly report", task.getTitle());
        assertEquals("Include project progress", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(newDueDate, task.getDueDate());
    }
}
