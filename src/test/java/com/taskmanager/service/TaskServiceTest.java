
package com.taskmanager.service;

import com.taskmanager.dto.CreateTaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @Test
    void shouldCreateTaskWithPendingStatus() {
        CreateTaskRequest request = new CreateTaskRequest(
                "Study Spring Boot",
                "Prepare for the interview",
                null,
                null
        );

        when(repository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = service.create(request);

        assertEquals(TaskStatus.PENDING, response.status());
        verify(repository).save(any(Task.class));
    }

    @Test
    void shouldThrowExceptionWhenTaskDoesNotExist() {
        Long taskId = 999L;
        when(repository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> service.findById(taskId)
        );
    }
}
