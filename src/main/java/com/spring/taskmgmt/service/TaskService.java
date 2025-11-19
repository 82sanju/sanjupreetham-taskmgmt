package com.spring.taskmgmt.service;

import com.spring.taskmgmt.exception.ResourceNotFoundException;
import com.spring.taskmgmt.model.Status;
import com.spring.taskmgmt.model.Task;
import com.spring.taskmgmt.model.User;
import com.spring.taskmgmt.repository.TaskRepository;
import com.spring.taskmgmt.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }
    public List<Task> getTasksByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }
    public List<Task> getTasksByDueDate(LocalDate date) {
        return taskRepository.findByDueDate(date);
    }

    public List<Task> getTasksByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        return taskRepository.findByUser(user);
    }

    public Task createTaskForUser(Long userId, Task task) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        task.setId(null);
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task existing = getTaskById(id);

        existing.setTitle(taskDetails.getTitle());
        existing.setDescription(taskDetails.getDescription());
        existing.setPriority(taskDetails.getPriority());
        existing.setStatus(taskDetails.getStatus());
        existing.setDueDate(taskDetails.getDueDate());

        if (taskDetails.getUser() != null && taskDetails.getUser().getId() != null) {
            Long newUserId = taskDetails.getUser().getId();
            User user = userRepository.findById(newUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + newUserId));
            existing.setUser(user);
        }

        return taskRepository.save(existing);
    }

    public void deleteTask(Long id) {
        Task existing = getTaskById(id);
        taskRepository.delete(existing);
    }
}
