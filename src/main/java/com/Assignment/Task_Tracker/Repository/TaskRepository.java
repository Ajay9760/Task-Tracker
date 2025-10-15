package com.Assignment.Task_Tracker.Repository;


import com.Assignment.Task_Tracker.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByProjectId(String projectId);
    List<Task> findByAssignedToId(String userId);
    List<Task> findByStatus(String status);

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.status = :status")
    List<Task> findByProjectIdAndStatus(@Param("projectId") String projectId,
                                        @Param("status") String status);

    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Task> searchTasks(@Param("search") String search);
}