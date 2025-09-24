package com.example.TasklistApi.controller;

import com.example.TasklistApi.dto.TaskDTO;
import com.example.TasklistApi.model.TaskStatus;
import com.example.TasklistApi.service.TaskService;
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
public class TaskController {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    
    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
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

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(
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

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
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

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, 
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

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskDTO> markTaskAsCompleted(@PathVariable Long id) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
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
