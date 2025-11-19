package com.spring.taskmgmt.service;

import com.spring.taskmgmt.exception.ResourceNotFoundException;
import com.spring.taskmgmt.model.User;
import com.spring.taskmgmt.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public User createUser(User user) {
        user.setId(null);
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User existing = getUserById(id);
        existing.setName(userDetails.getName());
        existing.setEmail(userDetails.getEmail());
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        User existing = getUserById(id);
        userRepository.delete(existing); // cascades to tasks
    }
}
