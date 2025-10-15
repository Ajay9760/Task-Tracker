package com.Assignment.Task_Tracker.Repository;

import com.Assignment.Task_Tracker.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    Optional<Team> findByName(String name);
    boolean existsByName(String name);
}