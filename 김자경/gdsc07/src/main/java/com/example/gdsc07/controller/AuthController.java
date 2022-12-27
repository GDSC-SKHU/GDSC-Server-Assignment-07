package com.example.gdsc07.controller;

import com.example.gdsc07.entity.Role;
import com.example.gdsc07.entity.User;
import com.example.gdsc07.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 메인 페이지로 이동하는 url
    @GetMapping
    public String index() {
        return "메인 페이지 입니다.";
    }

    // 유저 페이지로 이동하는 url
    @GetMapping("/user")
    public String user() {
        return "유저 페이지 입니다.";
    }

    // 어드민 페이지로 이동하는 url
    @GetMapping("/admin")
    public String admin() {
        return "어드민 페이지 입니다.";
    }

    // 회원가입
    // @PostMapping 권장 ➜ 여기서는 간단하게 구현할 예정이라 @GetMapping 사용
    // param 형태 json 형태 아무거나 가능 ➜ 여기서는 param 사용
    @GetMapping("/signup")
    // 사용자 이름, 비밀번호, 권한 유무를 받음
    public String signUp(@RequestParam String username, @RequestParam String password, @RequestParam boolean isAdmin) {
        // 권한 목록을 Set에 저장
        Set<Role> set = new HashSet<>();
        // 기본적으로 user에게 권한을 부여
        set.add(Role.ROLE_USER);

        // isAdmin이 true면 set에 넣음
        if (isAdmin) set.add(Role.ROLE_ADMIN);

        // 사용자 객체 생성
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password)) // 그대로 넣으면 비밀번호가 보이니까 인코더
                .roles(set)
                .build();

        // 실행 중(transaction)에 data를 즉시 flush
        userRepository.saveAndFlush(user);

        return user.getUsername() + "님 회원가입 완료";
    }

    // 내 정보 확인
    // @AuthenticationPrincipal로 필터링된 UserDetails를 불러올 수 있음
    @GetMapping("/myinfo")
    public String myInfo(@AuthenticationPrincipal User user) {
        return (user == null) ? "유저 정보 없음" : user.getAuthorities().toString();
    }
}