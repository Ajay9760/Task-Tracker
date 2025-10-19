package com.Assignment.Task_Tracker.Repository;

import com.Assignment.Task_Tracker.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    @Query("SELECT t FROM Task t WHERE t.team.id = :teamId")
    List<Task> findByProjectId(@Param("teamId") String teamId);
    @Query("SELECT t FROM Task t WHERE t.assignedTo.id = :userId")
    List<Task> findByAssignedToId(@Param("userId") String userId);

    @Query("SELECT t FROM Task t WHERE t.status = :status")
    List<Task> findByStatus(@Param("status") Task.TaskStatus status);
    @Query("SELECT t FROM Task t WHERE t.team.id = :teamId AND t.status = :status")
    List<Task> findByProjectIdAndStatus(
            @Param("teamId") String teamId,
            @Param("status") Task.TaskStatus status
    );
    
    /**
     * Finds all tasks associated with a specific team.
     *
     * @param teamId the ID of the team
     * @return a list of tasks associated with the team
     */
    List<Task> findByTeamId(String teamId);

    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Task> searchTasks(@Param("search") String search);
}