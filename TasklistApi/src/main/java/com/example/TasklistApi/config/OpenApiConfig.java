package com.example.TasklistApi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "TaskList API",
        version = "1.0.0",
        description = "A comprehensive Task Management REST API built with Spring Boot",
        contact = @Contact(
            name = "TaskList API Team",
            email = "support@tasklistapi.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8081",
            description = "Development Server"
        )
    },
    tags = {
        @Tag(name = "Tasks", description = "Task management operations")
    }
)
public class OpenApiConfig {
}