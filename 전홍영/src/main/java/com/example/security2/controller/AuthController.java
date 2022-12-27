package com.example.security2.controller;

import com.example.security2.entity.Role;
import com.example.security2.entity.User;
import com.example.security2.repository.UserRepository;
import com.example.security2.service.AuthService;
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
    public String index() {
        return "메인 페이지 입니다.";
    }

    @GetMapping("/user")
    public String user() {
        return "유저페이지 입니다.";
    }

    @GetMapping("/admin")
    public String admin() {
        return "어드민 페이지 입니다.";
    }

    //회원가입
    @GetMapping("/signup")
    public String 회원가입(@RequestParam String username, @RequestParam String password, @RequestParam boolean isAdmin) {
        Set<Role> set = new HashSet<>();//Role 집합 Set
        set.add(Role.ROLE_USER);//set을 USER로 설정

        if(isAdmin) set.add(Role.ROLE_ADMIN);//만약 isAdmin이 true라면 set에 ADMIN 추가
        //회원가입할 user 생성
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(set)
                .build();
        //새로 생성된 user 저장
        userRepository.saveAndFlush(user);//save vs saveFlush @Transactional에 따라 달라진다.

        return user.getUsername() + "님 가입 완료";

    }
    //나의 정보를 알려주는 myinfo
    @GetMapping("/myinfo")
    public String myinfo(@AuthenticationPrincipal User user) {//@AuthenticationPrincipal은 세션 정보를 알려주는 어노테이션
        return (user == null) ? "유저 정보 없음" : user.getAuthorities().toString();
    }
}
