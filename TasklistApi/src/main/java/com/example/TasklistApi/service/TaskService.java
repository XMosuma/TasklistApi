package com.example.TasklistApi.service;

import com.example.TasklistApi.dto.TaskDTO;
import com.example.TasklistApi.model.Task;
import com.example.TasklistApi.model.TaskStatus;
import com.example.TasklistApi.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    
    @Autowired
    private TaskRepository taskRepository;

    public TaskDTO createTask(TaskDTO taskDTO) {
        logger.info("Creating new task: {}", taskDTO.getTitle());
        
        Task task = convertToEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        
        logger.debug("Task created successfully with ID: {}", savedTask.getId());
        return convertToDTO(savedTask);
    }

    public List<TaskDTO> getAllTasks() {
        logger.info("Fetching all tasks");
        
        List<Task> tasks = taskRepository.findAll();
        logger.debug("Found {} tasks", tasks.size());
        
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByStatus(TaskStatus status) {
        logger.info("Fetching tasks with status: {}", status);
        
        List<Task> tasks = taskRepository.findByStatus(status);
        logger.debug("Found {} tasks with status {}", tasks.size(), status);
        
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TaskDTO> getTaskById(Long id) {
        logger.info("Fetching task with ID: {}", id);
        
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            logger.debug("Task found with ID: {}", id);
            return task.map(this::convertToDTO);
        } else {
            logger.warn("Task not found with ID: {}", id);
            return Optional.empty();
        }
    }

    public Optional<TaskDTO> updateTask(Long id, TaskDTO taskDTO) {
        logger.info("Updating task with ID: {}", id);
        
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setDueDate(taskDTO.getDueDate());
            if (taskDTO.getStatus() != null) {
                task.setStatus(taskDTO.getStatus());
            }
            
            Task updatedTask = taskRepository.save(task);
            logger.debug("Task updated successfully with ID: {}", id);
            return Optional.of(convertToDTO(updatedTask));
        } else {
            logger.warn("Cannot update - Task not found with ID: {}", id);
            return Optional.empty();
        }
    }

    public TaskDTO markTaskAsCompleted(Long id) {
        logger.info("Marking task as completed with ID: {}", id);
        
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setStatus(TaskStatus.COMPLETED);
            Task updatedTask = taskRepository.save(task);
            
            logger.debug("Task marked as completed with ID: {}", id);
            return convertToDTO(updatedTask);
        } else {
            logger.error("Cannot mark as completed - Task not found with ID: {}", id);
            throw new RuntimeException("Task not found with ID: " + id);
        }
    }

    public boolean deleteTask(Long id) {
        logger.info("Deleting task with ID: {}", id);
        
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            logger.debug("Task deleted successfully with ID: {}", id);
            return true;
        } else {
            logger.warn("Cannot delete - Task not found with ID: {}", id);
            return false;
        }
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        if (taskDTO.getStatus() != null) {
            task.setStatus(taskDTO.getStatus());
        }
        return task;
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setCreatedAt(task.getCreatedAt());
        taskDTO.setUpdatedAt(task.getUpdatedAt());
        return taskDTO;
    }
}