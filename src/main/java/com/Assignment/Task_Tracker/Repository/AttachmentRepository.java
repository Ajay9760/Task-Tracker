package com.Assignment.Task_Tracker.Repository;

import com.Assignment.Task_Tracker.Entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, String> {
}