package com.gdsc.security.controller;

import com.gdsc.security.domain.entity.Role;
import com.gdsc.security.domain.entity.User;
import com.gdsc.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository; // UserRepository 불러오기
    private final BCryptPasswordEncoder passwordEncoder; // Bean 불러오기

    @GetMapping
    public String index() {
        return "메인 페이지";
    }

    @GetMapping("/user")
    public String user() {
        return "유저 페이지";
    }

    @GetMapping("/admin")
    public String admin() {
        return "어드민 페이지";
    }

    // @AuthenticationPrincipal로 필터링된 UserDetails를 불러올 수 있음.
    @GetMapping("/myinfo") // 내 정보
    public String myInfo(@AuthenticationPrincipal User user) {
        return (user == null) ? "유저 정보 없음" : user.getAuthorities().toString();
        // null 이면 유저정보 없음 출력, 있으면 그 유저의 권한 목록을 출력
    }

    @GetMapping("/signup") // 원래는 GetMapping 사용 x, 간단한 연습을 위해 사용, Controller에서 구현도 원래는 X
    // username, password, isAdmin(어드민 여부)를 입력 받음)
    public String signUp(@RequestParam String username, @RequestParam String password, @RequestParam boolean isAdmin) {
        Set<Role> set = new HashSet<>();
        set.add(Role.ROLE_USER);

        if (isAdmin) set.add(Role.ROLE_ADMIN);

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password)) // 암호화
                .roles(set)
                .build();

        userRepository.saveAndFlush(user); // 즉시 데이터를 flush, save는 오류났을 때 flush 안하지만 saveAndFlush는 바로 flush한다.

        return user.getUsername()+"님 회원가입 성공";
    }
}
