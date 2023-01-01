package com.example.security4.controller;

import com.example.security4.entity.Role;
import com.example.security4.entity.User;
import com.example.security4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String index(){
        return "메인 페이지입니다.";
    }

    @GetMapping("/user")
    public String user(){
        return "유저 페이지입니다.";
    }

    @GetMapping("/admin")
    public String admin(){
        return "어드민 페이지입니다.";
    }
    @GetMapping("/signup")
    public String 회원가입(@RequestParam String username, @RequestParam String password, @RequestParam boolean isAdmin){
        Set<Role> set = new HashSet<>();
        set.add(Role.ROLE_USER);

        if(isAdmin) set.add(Role.ROLE_ADMIN);

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(set)
                .build();

        userRepository.saveAndFlush(user);
        return user.getUsername() + "님 가입 완료";
    }
    @GetMapping("/myinfo")
    public String myinfo(@AuthenticationPrincipal User user){
        return (user != null) ? user.getUsername():"인증 정보가 없어요";
    }
}
