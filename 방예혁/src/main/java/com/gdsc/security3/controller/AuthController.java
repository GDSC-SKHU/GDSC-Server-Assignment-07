package com.gdsc.security3.controller;

import com.gdsc.security3.domain.entity.Role;
import com.gdsc.security3.domain.entity.User;
import com.gdsc.security3.domain.repository.UserRepository;
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
    // user 객체가 비었으면 없다고 출력, 있으면 그 유저의 권한(Role) 목록을 출력함
    @GetMapping("/myinfo")
    public String myInfo(@AuthenticationPrincipal User user) {
        return (user == null) ? "유저 정보 없음" : user.getAuthorities().toString();
    }

    // RequestParam 형식으로 username, password, 어드민 여부를 입력 받음
    @GetMapping("/signup")
    public String signUp(@RequestParam String username, @RequestParam String password, @RequestParam boolean isAdmin) {
        // Role을 set에 저장
        Set<Role> set = new HashSet<>();
        set.add(Role.ROLE_USER); // 기본적으로 ROLE_USER 권한은 줌

        // isAdmin: true(1) -> ROLE_ADMIN 추가
        if(isAdmin) {
            set.add(Role.ROLE_ADMIN);
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password)) // password 암호화, return-type: String
                .roles(set)
                .build();

        userRepository.saveAndFlush(user); // 실행 중에 즉시 데이터를 flush

        return "회원가입 성공";
    }
}