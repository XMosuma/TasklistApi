package com.example.TasklistApi.repository;

import com.example.TasklistApi.model.Task;
import com.example.TasklistApi.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByStatusIn(List<TaskStatus> statuses);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksByDateRange(@Param("startDate") LocalDateTime startDate, 
                                  @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM Task t WHERE t.status = :status ORDER BY t.dueDate ASC")
    List<Task> findByStatusOrderByDueDateAsc(@Param("status") TaskStatus status);
}
