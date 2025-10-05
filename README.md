# TasklistApi
Spring Boot Tasklist API


# Spring Boot Tasklist API - Project Structure

TasklistApi/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── tasklistapi/
│   │   │               ├── TasklistApiApplication.java
│   │   │               ├── controller/
│   │   │               │   └── TaskController.java
│   │   │               ├── dto/
│   │   │               │   └── TaskDTO.java
│   │   │               ├── model/
│   │   │               │   ├── Task.java
│   │   │               │   └── TaskStatus.java
│   │   │               ├── repository/
│   │   │               │   └── TaskRepository.java
│   │   │               └── service/
│   │   │                   └── TaskService.java
│   │   └── resources/
│   │       ├── application.properties
│   └── test/
└── README.md


🎯 Core Features Implemented:
✅ Task Management:

Create tasks with due dates
List all tasks with optional status filtering
Mark tasks as completed
Full CRUD operations (Create, Read, Update, Delete)

✅ PostgreSQL Integration:

JPA entities with proper relationships
Repository layer with custom queries
Database configuration for PostgreSQL

✅ Comprehensive Logging:

Info, debug, warning, and error logs
Separate logging levels for development/production
Request/response logging in controllers

✅ Production Ready:

Input validation with proper error handling
DTO pattern for clean API contracts
Service layer architecture
CORS configuration for frontend integration

📋 API Endpoints:

POST /api/tasks - Create a new task
GET /api/tasks - Get all tasks (with optional status filter)
GET /api/tasks/{id} - Get task by ID
PUT /api/tasks/{id} - Update task
PATCH /api/tasks/{id}/complete - Mark task as completed
DELETE /api/tasks/{id} - Delete task


## Create Task:
{
  "title": "Complete Spring Boot project",
  "description": "Build a tasklist API with all features",
  "dueDate": "2024-01-15T10:30:00"
}

## Update Task:

{
  "title": "Updated task title",
  "description": "Updated description",
  "dueDate": "2024-01-20T15:00:00",
  "status": "IN_PROGRESS"
}