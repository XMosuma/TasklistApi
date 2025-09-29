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
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
└── README.md
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