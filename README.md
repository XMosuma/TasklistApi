# TaskList API

A comprehensive RESTful API for task management built with Spring Boot, featuring JWT authentication, audit logging, and PostgreSQL database integration.

## Features

- **CRUD Operations**: Create, Read, Update, Delete tasks
- **Task Status Management**: PENDING, IN_PROGRESS, COMPLETED, CANCELLED
- **JWT Authentication**: Secure user registration and login
- **Audit Logging**: Track who created, updated, or deleted tasks
- **Status Filtering**: Filter tasks by status
- **PostgreSQL Database**: Persistent data storage
- **Swagger/OpenAPI Documentation**: Interactive API documentation
- **Docker Support**: Containerized deployment
- **CI/CD Pipeline**: GitHub Actions for automated testing and building

## Tech Stack

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Security** with JWT
- **Spring Data JPA**
- **PostgreSQL 15**
- **Maven**
- **Docker & Docker Compose**
- **Swagger/OpenAPI 3.0**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose (for containerized setup)
- PostgreSQL 15 (if running without Docker)

## Project Structure

```
TasklistApi/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/TasklistApi/
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── dto/            # Data Transfer Objects
│   │   │       ├── model/          # Entity classes
│   │   │       ├── repository/     # JPA repositories
│   │   │       ├── security/       # JWT security components
│   │   │       └── service/        # Business logic
│   │   └── resources/
│   │       └── application.properties
│   └── test/                       # Unit tests
├── .github/
│   └── workflows/
│       └── ci-cd.yml              # GitHub Actions workflow
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Getting Started

### Option 1: Run with Docker (Recommended)

1. **Clone the repository**
```bash
git clone https://github.com/XMosuma/TasklistApi.git
cd TasklistApi
```

2. **Start PostgreSQL**
```bash
docker run --name postgres-tasklist \
  -e POSTGRES_PASSWORD=mypassword \
  -e POSTGRES_DB=tasklist_db \
  -p 5432:5432 \
  -d postgres:15
```

3. **Build and run the application**
```bash
# Build JAR
mvn clean package -DskipTests

# Build Docker image
docker build -t tasklist-api:latest .

# Run container
docker run -d \
  --name tasklist-api \
  --link postgres-tasklist:postgres \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-tasklist:5432/tasklist_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=mypassword \
  -p 8081:8081 \
  tasklist-api:latest
```

4. **Access the application**
- API: http://localhost:8081/api/tasks
- Swagger UI: http://localhost:8081/swagger-ui.html

### Option 2: Run with Docker Compose

1. **Create docker-compose.yml**
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: postgres-tasklist
    environment:
      POSTGRES_DB: tasklist_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: mypassword
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  api:
    build: .
    container_name: tasklist-api
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/tasklist_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: mypassword
    ports:
      - "8081:8081"

volumes:
  postgres_data:
```

2. **Run**
```bash
docker-compose up -d
```

### Option 3: Run Locally (Without Docker)

1. **Install PostgreSQL 15**

2. **Create database**
```sql
CREATE DATABASE tasklist_db;
```

3. **Update application.properties**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tasklist_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

4. **Run the application**
```bash
mvn spring-boot:run
```

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Tasks

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks |
| GET | `/api/tasks?status=PENDING` | Filter tasks by status |
| GET | `/api/tasks/{id}` | Get task by ID |
| POST | `/api/tasks` | Create new task |
| PUT | `/api/tasks/{id}` | Update task |
| PATCH | `/api/tasks/{id}/complete` | Mark task as completed |
| DELETE | `/api/tasks/{id}` | Delete task |

### Audit Logs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/audit` | Get all audit logs |
| GET | `/api/audit/user/{username}` | Get logs by user |
| GET | `/api/audit/task/{taskId}` | Get logs for specific task |
| GET | `/api/audit/action/{action}` | Get logs by action type |

## Usage Examples

### 1. Register a User

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "password123"
  }'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "john",
  "email": "john@example.com"
}
```

### 3. Create a Task (with JWT token)

```bash
curl -X POST http://localhost:8081/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Complete project documentation",
    "description": "Write comprehensive API documentation",
    "dueDate": "2025-01-25T17:00:00"
  }'
```

### 4. Get All Tasks

```bash
curl -X GET http://localhost:8081/api/tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 5. Update Task

```bash
curl -X PUT http://localhost:8081/api/tasks/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Updated task title",
    "description": "Updated description",
    "dueDate": "2025-01-26T18:00:00",
    "status": "IN_PROGRESS"
  }'
```

### 6. Mark as Completed

```bash
curl -X PATCH http://localhost:8081/api/tasks/1/complete \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 7. View Audit Logs

```bash
curl -X GET http://localhost:8081/api/audit/task/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Configuration

### Database Configuration

Edit `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/tasklist_db
spring.datasource.username=postgres
spring.datasource.password=mypassword

# Server
server.port=8081

# JWT
jwt.secret=your_secret_key_here
jwt.expiration=86400000
```

### Environment Variables

For Docker deployment, use environment variables:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/tasklist_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=mypassword
JWT_SECRET=your_production_secret
```

## Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report

# Skip tests during build
mvn clean package -DskipTests
```

## CI/CD Pipeline

The project includes GitHub Actions workflow for:
- Automated testing with PostgreSQL
- Building JAR artifact
- Creating Docker images
- Pushing to GitHub Container Registry

Workflow file: `.github/workflows/ci-cd.yml`

## Deployment

### Deploy to Linux VM

1. **SSH into your server**
```bash
ssh user@your-server-ip
```

2. **Pull and run**
```bash
docker pull ghcr.io/xmosuma/tasklistapi:main
docker run -d --name tasklist-api -p 8081:8081 [environment variables]
```

## Troubleshooting

### Database Connection Issues

```bash
# Check if PostgreSQL is running
docker ps

# View PostgreSQL logs
docker logs postgres-tasklist

# Test connection
docker exec -it postgres-tasklist psql -U postgres -d tasklist_db
```

### Application Issues

```bash
# View application logs
docker logs -f tasklist-api

# Restart application
docker restart tasklist-api

# Check application health
curl http://localhost:8081/api/tasks
```

### Port Already in Use

```bash
# Find process using port 8081
netstat -ano | findstr :8081

# Stop the container
docker stop tasklist-api
docker rm tasklist-api
```

## Database Schema

### Tasks Table
- `id` (BIGSERIAL, Primary Key)
- `title` (VARCHAR, NOT NULL)
- `description` (TEXT)
- `due_date` (TIMESTAMP, NOT NULL)
- `status` (VARCHAR)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)
- `created_by` (VARCHAR)
- `last_modified_by` (VARCHAR)

### Users Table
- `id` (BIGSERIAL, Primary Key)
- `username` (VARCHAR, UNIQUE)
- `email` (VARCHAR)
- `password` (VARCHAR)
- `created_at` (TIMESTAMP)

### Audit Logs Table
- `id` (BIGSERIAL, Primary Key)
- `username` (VARCHAR)
- `action` (VARCHAR)
- `entity_type` (VARCHAR)
- `entity_id` (BIGINT)
- `details` (TEXT)
- `timestamp` (TIMESTAMP)

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Author

**Xolani Mosuma**
- GitHub: [@XMosuma](https://github.com/XMosuma)

## Acknowledgments

- Spring Boot Documentation
- PostgreSQL Documentation
- Docker Documentation
- JWT.io