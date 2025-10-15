package com.Assignment.Task_Tracker.Service;

import com.Assignment.Task_Tracker.Entity.Task;
import com.Assignment.Task_Tracker.Repository.TaskRepository;
import com.Assignment.Task_Tracker.Service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class TaskServiceTest {
    @Test
    void basicCrud() {
        TaskRepository repo = Mockito.mock(TaskRepository.class);
        TaskService service = new TaskService(repo, null, null);
        Task t = new Task();
        t.setId("1L");
        when(repo.findById("1L")).thenReturn(Optional.of(t));
    }
}
