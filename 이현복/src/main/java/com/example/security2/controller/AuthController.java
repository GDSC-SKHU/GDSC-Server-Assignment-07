package com.example.security2.controller;


import com.example.security2.entity.Role;
import com.example.security2.entity.User;
import com.example.security2.repository.UserRepository;
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
    private  final UserRepository userRepository;
    
    // 메인페이지 이동
    @GetMapping("/")
    public String index() {
        return "메인 페이지 입니다.";
    }

    // 유저 페이지 이동
    // SecurityConfig에서 user에 대한 권한이 있는지 확인 후 접속 가능
    @GetMapping("/user")
    public String user() {
        return  "유저 페이지 입니다.";
    }

    // admin 페이지 이돟
    // SecurityConfig에서 admin에 대한 권한이 있는지 확인 후 접속 가능
    @GetMapping("/admin")
    public String admin() {
        return "어드민 페이지 입니다.";
    }


    // 회원가입 방법
    // http://localhost:8080/signup?username=test&password=test&isAdmin=1

    // username과 password, 권환을 가질 수 있는 isAdmin uri를 입력 받아 회원가입 시도
    @GetMapping("/signup")
    public String 회원가입(@RequestParam String username, @RequestParam String password, @RequestParam boolean isAdmin) {

        // 권한을 넣어줄 수 있는 set컬렉션을 통해 권한 부여
        Set<Role> set = new HashSet<>();

        // 유저에 대한 권한 추가
        set.add(Role.ROLE_USER);

        // isAdmin이 true이면 admin 권한 추가
        if (isAdmin) set.add(Role.ROLE_ADMIN);


        // user정보 저장
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(set)
                .build();

        // user에 정보 저장
        userRepository.saveAndFlush(user);
        return user.getUsername() + "님 가입 완료";
    }


    // AuthenticationPrincipal 어노테이션 : userdetails 불러오기
    @GetMapping("/myinfo")
    public String myinfo(@AuthenticationPrincipal User user) {
        // user여부 ? true시  user이름 출력 false 시 : "인증정보 없음" 출력 
        return (user != null) ? user.getUsername() : "인증 정보가 없어요";
    }
}
