package com.spring.taskmgmt.repository;

import com.spring.taskmgmt.model.Status;
import com.spring.taskmgmt.model.Task;
import com.spring.taskmgmt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);
    List<Task> findByStatus(Status status);
    List<Task> findByDueDate(LocalDate dueDate);

}
