package com.example.gdsc07.repository;

import com.example.gdsc07.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// username을 통해 사용자를 찾는 findByUsername 메소드
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}