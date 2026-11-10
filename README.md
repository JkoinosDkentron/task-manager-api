
# Task Manager API

REST API for managing personal tasks, developed with Java 21 and Spring Boot 3.

This project is part of a Java and Spring Boot technical assessment. It demonstrates REST API development, object-oriented programming, data persistence, input validation, centralized exception handling, automated testing, and Git version control.

## Technologies

- Java 21
- Spring Boot 3.x
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Jakarta Bean Validation
- H2 Database
- Gradle
- JUnit 5
- Mockito
- MockMvc
- IntelliJ IDEA HTTP Client

## Architecture

The application follows a simple layered architecture:

    HTTP Client
        |
        v
    TaskController
        |
        v
    TaskService
        |
        v
    TaskRepository
        |
        v
    H2 Database

Responsibilities:

- Controller: HTTP endpoints and request validation.
- DTO: Request and response contracts.
- Service: Application operations and transaction management.
- Repository: Data access through Spring Data JPA.
- Entity: Task data model and controlled updates.
- Exception: Centralized handling of application errors.

The application uses constructor dependency injection.

## Task Model

| Field | Type | Description |
|---|---|---|
| id | Long | Automatically generated identifier |
| title | String | Required, 3 to 100 characters |
| description | String | Optional, maximum 500 characters |
| status | TaskStatus | PENDING, IN_PROGRESS, COMPLETED |
| dueDate | LocalDate | Optional; cannot be in the past on creation |
| createdAt | LocalDateTime | Automatically generated timestamp |

When no status is provided during creation, PENDING is used.

## Requirements

- JDK 21
- Git
- Internet connection for initial Gradle dependency downloads
- IntelliJ IDEA recommended

No external database installation is required.

## Running the Application

On Windows, open PowerShell at the project root.

### 1. Execute the tests

    .\gradlew.bat clean test

### 2. Start the application

    .\gradlew.bat bootRun

The REST API will be available at:

    http://localhost:8080

### 3. Stop the application

Press Ctrl+C in the terminal running Spring Boot.

## API Endpoints

| Method | Endpoint | Description | Successful HTTP |
|---|---|---|---|
| POST | /api/tasks | Create a task | 201 |
| GET | /api/tasks | Retrieve all tasks | 200 |
| GET | /api/tasks?status=COMPLETED | Filter tasks by status | 200 |
| GET | /api/tasks/{id} | Retrieve a task by ID | 200 |
| PUT | /api/tasks/{id} | Update task information | 200 |
| PATCH | /api/tasks/{id}/status | Update only the task status | 200 |
| DELETE | /api/tasks/{id} | Delete a task | 204 |

Missing task identifiers return 404.

Invalid request bodies return 400.

## Example: Create a Task

Request:

    POST /api/tasks
    Content-Type: application/json

    {
      "title": "Prepare technical interview",
      "description": "Review Java and Spring Boot",
      "dueDate": "2099-12-15"
    }

Example successful response:

    {
      "id": 1,
      "title": "Prepare technical interview",
      "description": "Review Java and Spring Boot",
      "status": "PENDING",
      "dueDate": "2099-12-15",
      "createdAt": "2026-11-10T12:00:00"
    }

The ID and creation timestamp are generated at runtime.

## Example: Update Task Status

Request:

    PATCH /api/tasks/1/status
    Content-Type: application/json

    {
      "status": "COMPLETED"
    }

The response contains the updated task.

## Validation

The application validates:

- Required title.
- Title length between 3 and 100 characters.
- Description length up to 500 characters.
- Valid TaskStatus enum values.
- Valid due date on task creation.
- Required status for status updates.

The API returns HTTP 400 when validation fails.

## Error Handling

The application uses @RestControllerAdvice for centralized error handling.

An example response for a nonexistent task:

    {
      "timestamp": "2026-11-10T12:00:00",
      "status": 404,
      "error": "Task not found with ID: 999"
    }

Field validation errors also contain a fields object indicating invalid properties.

## Manual API Testing

The project includes:

    src/main/resources/request.http

To execute requests:

1. Start the Spring Boot application.
2. Open request.http in IntelliJ IDEA.
3. Execute the CREATE TASK request.
4. Execute GET, PUT, PATCH and filtering requests.
5. Execute invalid request scenarios.
6. Execute DELETE last.

The HTTP collection saves the generated task identifier for subsequent requests.

## H2 Database Console

While the application is running, open:

    http://localhost:8080/h2-console

Connection configuration:

    JDBC URL: jdbc:h2:mem:taskdb
    User Name: sa
    Password: [empty]

After connecting, run:

    SELECT * FROM TASKS;

The default H2 database is in-memory.

Data is reset when the application stops.

This configuration is intended for local development and assessment testing.

## Automated Testing

Run all automated tests:

    .\gradlew.bat clean test

Run only the REST integration tests:

    .\gradlew.bat test --tests "com.taskmanager.controller.TaskControllerIntegrationTest"

Automated tests cover domain behavior, service behavior, request validation and REST API operations.

The test suite uses JUnit 5, Mockito and MockMvc.

## Git Workflow

Development uses a local GitFlow-inspired workflow:

- main: Stable release branch.
- develop: Integration branch.
- feature/*: Isolated feature development.

Each completed feature is tested and merged into develop.

The repository is intentionally kept local during development.

## Technical Decisions

### Why Spring Data JPA?

It simplifies common database operations and provides repository abstractions.

### Why DTOs?

DTOs separate public API contracts from persistence entities and provide a clear location for request validation.

### Why H2?

H2 allows the assessment to run without installing or configuring an external database.

### Why constructor injection?

Constructor injection makes dependencies explicit and simplifies unit testing.

### Why PUT and PATCH?

PUT updates the complete editable representation of a task, while PATCH updates only its status.

### Why centralized error handling?

It provides consistent HTTP error responses and prevents repetitive exception handling across controllers.

## Possible Future Improvements

- PostgreSQL persistence profile.
- Pagination and sorting.
- Database migrations with Flyway.
- Authentication and authorization.
- OpenAPI/Swagger documentation.
- Additional validation edge-case tests.
- Docker support.
- CI/CD.

These improvements are outside the minimal requirements of the assessment.
