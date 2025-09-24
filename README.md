# TasklistApi
Spring Boot Tasklist API


# Spring Boot Tasklist API - Project Structure

```
tasklist-api/
│
├── pom.xml                                    # Maven configuration file
├── README.md                                  # Project documentation
├── .gitignore                                # Git ignore file
├── docker-compose.yml                        # Docker composition for PostgreSQL
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── tasklistapi/
│   │   │               │
│   │   │               ├── TasklistApiApplication.java       # Main Spring Boot Application
│   │   │               │
│   │   │               ├── model/                           # Entity Models
│   │   │               │   ├── Task.java                    # Task entity
│   │   │               │   └── TaskStatus.java              # Status enum
│   │   │               │
│   │   │               ├── repository/                      # Data Access Layer
│   │   │               │   └── TaskRepository.java          # Task repository interface
│   │   │               │
│   │   │               ├── service/                         # Business Logic Layer
│   │   │               │   └── TaskService.java             # Task service implementation
│   │   │               │
│   │   │               ├── controller/                      # REST API Controllers
│   │   │               │   └── TaskController.java          # Task REST controller
│   │   │               │
│   │   │               ├── dto/                            # Data Transfer Objects (Optional)
│   │   │               │   ├── TaskCreateRequest.java       # Create task request DTO
│   │   │               │   ├── TaskUpdateRequest.java       # Update task request DTO
│   │   │               │   └── TaskResponse.java            # Task response DTO
│   │   │               │
│   │   │               ├── exception/                       # Custom Exceptions
│   │   │               │   ├── TaskNotFoundException.java   # Task not found exception
│   │   │               │   └── GlobalExceptionHandler.java  # Global exception handler
│   │   │               │
│   │   │               └── config/                         # Configuration Classes
│   │   │                   ├── DatabaseConfig.java          # Database configuration
│   │   │                   └── CorsConfig.java             # CORS configuration
│   │   │
│   │   └── resources/
│   │       ├── application.properties         # Main application configuration
│   │       ├── application-dev.properties     # Development environment config
│   │       ├── application-prod.properties    # Production environment config
│   │       ├── logback-spring.xml            # Logging configuration
│   │       │
│   │       ├── static/                       # Static web resources (if any)
│   │       │   └── index.html                # Simple API documentation page
│   │       │
│   │       └── data.sql                      # Initial data (optional)
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── tasklistapi/
│       │               │
│       │               ├── TasklistApiApplicationTests.java # Main application test
│       │               │
│       │               ├── controller/                     # Controller tests
│       │               │   └── TaskControllerTest.java     # Task controller tests
│       │               │
│       │               ├── service/                        # Service tests
│       │               │   └── TaskServiceTest.java        # Task service tests
│       │               │
│       │               └── repository/                     # Repository tests
│       │                   └── TaskRepositoryTest.java     # Task repository tests
│       │
│       └── resources/
│           ├── application-test.properties    # Test environment configuration
│           └── test-data.sql                 # Test data
│
├── postman/                                  # Postman collection for API testing
│   └── Tasklist_API.postman_collection.json # Postman collection file
│
├── docs/                                     # Documentation
│   ├── api-documentation.md                 # API documentation
│   └── setup-guide.md                       # Setup and installation guide
│
└── scripts/                                 # Utility scripts
    ├── setup-database.sql                   # Database setup script
    ├── start-dev.sh                        # Development startup script
    └── deploy.sh                           # Deployment script
```

## Key Components Explained:

### 1. **Root Level Files**
- **pom.xml**: Maven dependencies and build configuration
- **docker-compose.yml**: PostgreSQL database setup
- **.gitignore**: Git ignore patterns for Java/Spring Boot projects

### 2. **Source Code Structure (src/main/java)**
- **model/**: Entity classes representing database tables
- **repository/**: Data access layer using Spring Data JPA
- **service/**: Business logic and transaction management
- **controller/**: REST API endpoints and request handling
- **dto/**: Data transfer objects for API requests/responses
- **exception/**: Custom exceptions and global error handling
- **config/**: Configuration classes for various aspects

### 3. **Resources (src/main/resources)**
- **application.properties**: Main configuration
- **Environment-specific configs**: Dev, prod, test configurations
- **logback-spring.xml**: Logging configuration
- **static/**: Static web content
- **data.sql**: Initial database data

### 4. **Test Structure (src/test/java)**
- **Unit tests**: For services, repositories, controllers
- **Integration tests**: End-to-end API testing
- **Test resources**: Test-specific configurations and data

### 5. **Additional Folders**
- **postman/**: API testing collections
- **docs/**: Project documentation
- **scripts/**: Automation and deployment scripts

## Package Naming Convention:
- **com.example.tasklistapi**: Base package
- Follows standard Spring Boot layered architecture
- Clear separation of concerns
- Follows Java naming conventions

## Architecture Pattern:
This follows the **3-layer architecture**:
1. **Presentation Layer**: Controllers (REST API)
2. **Business Layer**: Services (Business Logic)
3. **Data Layer**: Repositories (Data Access)

This structure provides:
- ✅ Clean separation of concerns
- ✅ Easy testing and maintenance
- ✅ Scalable architecture
- ✅ Standard Spring Boot conventions
- ✅ Professional project organization