
package com.taskmanager.service;

import com.taskmanager.dto.*;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TaskResponse create(CreateTaskRequest request) {
        Task task = new Task(
                request.title(),
                request.description(),
                request.status(),
                request.dueDate()
        );

        return toResponse(repository.save(task));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> findAll(TaskStatus status) {
        List<Task> tasks = status == null
                ? repository.findAll()
                : repository.findByStatus(status);

        return tasks.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse findById(Long id) {
        return toResponse(findTask(id));
    }

    @Transactional
    public TaskResponse update(Long id, UpdateTaskRequest request) {
        Task task = findTask(id);

        task.update(
                request.title(),
                request.description(),
                request.status(),
                request.dueDate()
        );

        return toResponse(repository.save(task));
    }

    @Transactional
    public TaskResponse updateStatus(
            Long id,
            UpdateTaskStatusRequest request
    ) {
        Task task = findTask(id);
        task.updateStatus(request.status());

        return toResponse(repository.save(task));
    }

    @Transactional
    public void delete(Long id) {
        Task task = findTask(id);
        repository.delete(task);
    }

    private Task findTask(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(id)
                );
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate(),
                task.getCreatedAt()
        );
    }
}
