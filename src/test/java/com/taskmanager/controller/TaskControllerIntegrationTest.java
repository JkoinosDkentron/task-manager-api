
package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.entity.Task;
import com.taskmanager.entity.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldCreateTaskWithPendingStatus() throws Exception {

        String request = """
                {
                  "title": "Prepare technical interview",
                  "description": "Review Spring Boot"
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title")
                        .value("Prepare technical interview"))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"));
    }

    @Test
    void shouldRejectInvalidTaskTitle() throws Exception {

        String request = """
                {
                  "title": "AB"
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.title").exists());
    }

    @Test
    void shouldReturnNotFoundForMissingTask() throws Exception {

        mockMvc.perform(get("/api/tasks/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldListTasksFilteredByStatus() throws Exception {

        repository.save(new Task(
                "First pending task",
                null,
                TaskStatus.PENDING,
                null
        ));

        repository.save(new Task(
                "Completed task",
                null,
                TaskStatus.COMPLETED,
                null
        ));

        mockMvc.perform(get("/api/tasks")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status")
                        .value("COMPLETED"));
    }

    @Test
    void shouldUpdateTaskStatus() throws Exception {

        Task task = repository.save(new Task(
                "Prepare report",
                null,
                TaskStatus.PENDING,
                null
        ));

        String request = """
                {
                  "status": "COMPLETED"
                }
                """;

        mockMvc.perform(patch(
                        "/api/tasks/{id}/status",
                        task.getId()
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("COMPLETED"));
    }

    @Test
    void shouldDeleteTask() throws Exception {

        Task task = repository.save(new Task(
                "Delete temporary task",
                null,
                TaskStatus.PENDING,
                null
        ));

        mockMvc.perform(delete(
                        "/api/tasks/{id}",
                        task.getId()
                ))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(
                        "/api/tasks/{id}",
                        task.getId()
                ))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldUpdateTaskUsingPut() throws Exception {

        Task task = repository.save(new Task(
                "Original task",
                null,
                TaskStatus.PENDING,
                null
        ));

        String request = """
                {
                  "title": "Updated task title",
                  "description": "Updated description",
                  "status": "IN_PROGRESS"
                }
                """;

        mockMvc.perform(put("/api/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Updated task title"))
                .andExpect(jsonPath("$.description")
                        .value("Updated description"))
                .andExpect(jsonPath("$.status")
                        .value("IN_PROGRESS"));
    }

    @Test
    void shouldRejectPastDueDateWhenCreatingTask() throws Exception {

        String request = """
                {
                  "title": "Task with past deadline",
                  "dueDate": "%s"
                }
                """.formatted(LocalDate.now().minusDays(1));

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.dueDate").exists());
    }


    @Test
    void shouldRejectInvalidStatus() throws Exception {
        String request = """
                {
                    "status": "INVALID"
                }
                """;

        mockMvc.perform(patch("/api/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPersistUpdatedTask() throws Exception {
        Task task = repository.save(new Task(
                "Original task",
                null,
                TaskStatus.PENDING,
                null
        ));

        String request = """
                {
                    "title": "Updated task title",
                    "description": "Updated description",
                    "status": "COMPLETED"
                }
                """;

        mockMvc.perform(put("/api/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        mockMvc.perform(get("/api/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated task title"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

}
