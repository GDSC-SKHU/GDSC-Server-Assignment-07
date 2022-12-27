package com.example.security2.repository;

import com.example.security2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    // User이름을 찾을 수 있도록 하는 findByUsername
    Optional<User> findByUsername(String username);
}
