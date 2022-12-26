package com.gdsc.security2.controller;

import com.gdsc.security2.entity.Role;
import com.gdsc.security2.entity.User;
import com.gdsc.security2.repository.UserRepository;
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
    private final BCryptPasswordEncoder passwordEncoder;        // 비밀번호 암호화 클래스
    private final UserRepository userRepository;

    @GetMapping("/")
    public String index() {
        return "메인 페이지입니다.";
    }

    @GetMapping("/user")
    public String user() {
        return "유저 페이지입니다.";
    }

    @GetMapping("/admin")
    public String admin() {
        return "어드민 페이지입니다.";
    }


    // @AuthenticationPrincipal - 세션 정보 UserDetails에 접근할 수 있는 어노테이션
    // 로그인 세션 정보가 필요할 때 사용
    @GetMapping("/myinfo")
    public String myInfo(@AuthenticationPrincipal User user) {
        return (user == null) ? "인증 정보가 없어요" : user.getAuthorities().toString();    // getAuthorities()는 계정이 갖고 있는 권한 목록 리턴
    }

    // username, password, 관리자 권한 유무를 request parameter로 받음.
    @GetMapping("/signup")
    public String signUp(@RequestParam String username, @RequestParam String password, @RequestParam boolean isAdmin) {
        // 권한 목록을 담을 set에 기본적으로 USER 권한 추가
        Set<Role> set = new HashSet<>();
        set.add(Role.ROLE_USER);

        // isAdmin이 True일 경우 ADMIN 권한 추가
        if (isAdmin) set.add(Role.ROLE_ADMIN);

        // user 객체를 생성하고 그 객체를 DB에 저장
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(set)
                .build();

        userRepository.saveAndFlush(user);

        return "회원가입 성공";
    }
}
