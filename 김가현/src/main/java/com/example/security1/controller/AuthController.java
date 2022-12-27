package com.example.security1.controller;

import com.example.security1.domain.entitiy.Role;
import com.example.security1.domain.entitiy.User;
import com.example.security1.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository; //userRepository 불러오기
    private final BCryptPasswordEncoder passwordEncoder; //Bean을 가져온다

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
    //내 정보 확인(=UserDetails)
    @GetMapping("/myinfo")
    public String myInfo(@AuthenticationPrincipal User user) {
        return (user == null) ? "유저 정보 없음" : user.getAuthorities().toString();
        //(다른 표현) return (user == null) ? user.getAuthorities() : "인증 정보가 없어요"
    }

    //원래 Service쪽으로 빠지고 포스트맵핑+a 등이 필요하지만 일단 간단하게만
    @GetMapping("/signup")
    public String signUp(@RequestParam String username, @RequestParam String password, @RequestParam boolean isAdmin) {
        Set<Role> set = new HashSet<>();
        set.add(Role.ROLE_USER);

        if (isAdmin) set.add(Role.ROLE_ADMIN);

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(set)
                .build();

        userRepository.saveAndFlush(user); //바로 저장을 하겠다

        return user.getUsername() + "님 가입 완료";
    }
}
