package com.example.security1.domain.repository;

import com.example.security1.domain.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    //Optional=Nullexception 방지용
}