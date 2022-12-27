package com.example.security2.repository;

import com.example.security2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
    //Optional<T>는 null이 올 수 있는 값을 감싸는 Wrapper 클래스로,
    //참조하더라도 NPE가 발생하지 않도록 도와준다.
}
