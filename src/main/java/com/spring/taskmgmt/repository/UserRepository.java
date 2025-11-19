package com.spring.taskmgmt.repository;

import com.spring.taskmgmt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
