package com.example.TasklistApi.controller;

import com.example.TasklistApi.dto.TaskDTO;
import com.example.TasklistApi.model.TaskStatus;
import com.example.TasklistApi.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
@Tag(name = "Tasks", description = "Task management operations")
public class TaskController {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    
    @Autowired
    private TaskService taskService;

    @Operation(summary = "Create a new task", description = "Creates a new task with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task created successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @Parameter(description = "Task details", required = true)
            @Valid @RequestBody TaskDTO taskDTO) {
        logger.info("REST: Creating new task");
        
        try {
            TaskDTO createdTask = taskService.createTask(taskDTO);
            logger.info("REST: Task created successfully with ID: {}", createdTask.getId());
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("REST: Error creating task", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all tasks", description = "Retrieves all tasks, optionally filtered by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @Parameter(description = "Filter tasks by status", example = "PENDING")
            @RequestParam(required = false) TaskStatus status) {
        
        logger.info("REST: Fetching tasks with status filter: {}", status);
        
        try {
            List<TaskDTO> tasks;
            if (status != null) {
                tasks = taskService.getTasksByStatus(status);
            } else {
                tasks = taskService.getAllTasks();
            }
            
            logger.info("REST: Retrieved {} tasks", tasks.size());
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("REST: Error fetching tasks", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get task by ID", description = "Retrieves a specific task by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
        @ApiResponse(responseCode = "404", description = "Task not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(
            @Parameter(description = "Task ID", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("REST: Fetching task with ID: {}", id);
        
        try {
            Optional<TaskDTO> task = taskService.getTaskById(id);
            if (task.isPresent()) {
                logger.debug("REST: Task found with ID: {}", id);
                return new ResponseEntity<>(task.get(), HttpStatus.OK);
            } else {
                logger.warn("REST: Task not found with ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("REST: Error fetching task with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update task", description = "Updates an existing task with new details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task updated successfully",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
        @ApiResponse(responseCode = "404", description = "Task not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @Parameter(description = "Task ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated task details", required = true)
            @Valid @RequestBody TaskDTO taskDTO) {
        logger.info("REST: Updating task with ID: {}", id);
        
        try {
            Optional<TaskDTO> updatedTask = taskService.updateTask(id, taskDTO);
            if (updatedTask.isPresent()) {
                logger.info("REST: Task updated successfully with ID: {}", id);
                return new ResponseEntity<>(updatedTask.get(), HttpStatus.OK);
            } else {
                logger.warn("REST: Task not found for update with ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("REST: Error updating task with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Mark task as completed", description = "Marks a specific task as completed")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task marked as completed",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDTO.class))),
        @ApiResponse(responseCode = "404", description = "Task not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskDTO> markTaskAsCompleted(
            @Parameter(description = "Task ID", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("REST: Marking task as completed with ID: {}", id);
        
        try {
            TaskDTO completedTask = taskService.markTaskAsCompleted(id);
            logger.info("REST: Task marked as completed with ID: {}", id);
            return new ResponseEntity<>(completedTask, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.warn("REST: Task not found to mark as completed with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("REST: Error marking task as completed with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete task", description = "Deletes a specific task by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Task ID", required = true, example = "1")
            @PathVariable Long id) {
        logger.info("REST: Deleting task with ID: {}", id);
        
        try {
            boolean deleted = taskService.deleteTask(id);
            if (deleted) {
                logger.info("REST: Task deleted successfully with ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                logger.warn("REST: Task not found for deletion with ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("REST: Error deleting task with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}