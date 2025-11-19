package com.spring.taskmgmt.controller;

import com.spring.taskmgmt.dto.CreateTaskRequest;
import com.spring.taskmgmt.dto.UpdateTaskRequest;
import com.spring.taskmgmt.model.Status;
import com.spring.taskmgmt.model.Task;
import com.spring.taskmgmt.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Tasks", description = "Task management APIs")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // All tasks
    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    // Task by id
    @GetMapping("/tasks/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        try {
            Status enumStatus = Status.valueOf(status.toUpperCase());
            return ResponseEntity.ok(taskService.getTasksByStatus(enumStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/duedate/{date}")
    public ResponseEntity<?> getTasksByDueDate(@PathVariable String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            return ResponseEntity.ok(taskService.getTasksByDueDate(parsedDate));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid date format. Use yyyy-MM-dd");
        }
    }

    // All tasks for a user
    @GetMapping("/users/{userId}/tasks")
    public List<Task> getTasksByUser(@PathVariable Long userId) {
        return taskService.getTasksByUserId(userId);
    }

    // Create task for user
    @PostMapping("/users/{userId}/tasks")
    public ResponseEntity<Task> createTaskForUser(
            @PathVariable Long userId,
            @Valid @RequestBody CreateTaskRequest dto) {

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());

        Task created = taskService.createTaskForUser(userId, task);
        return ResponseEntity.ok(created);
    }

    // Update task
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest dto) {

        Task taskDetails = new Task();
        taskDetails.setTitle(dto.getTitle());
        taskDetails.setDescription(dto.getDescription());
        taskDetails.setPriority(dto.getPriority());
        taskDetails.setStatus(dto.getStatus());
        taskDetails.setDueDate(dto.getDueDate());

        Task updated = taskService.updateTask(id, taskDetails);
        return ResponseEntity.ok(updated);
    }

    // Delete task
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
