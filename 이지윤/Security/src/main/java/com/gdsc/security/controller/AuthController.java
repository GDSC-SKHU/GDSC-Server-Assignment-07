package com.gdsc.security.controller;


import com.gdsc.security.entity.Role;
import com.gdsc.security.entity.User;
import com.gdsc.security.repository.UserRepository;
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

    private  final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    //main page
    @GetMapping("/")
    public String index() {
        return "메인 페이지 입니다.";
    }

    //로그인 된 사용자의 권한이 user일 경우 접속
    //만약 정보가 없을 경우 login페이지로 넘어감.
    @GetMapping("/user")
    public String user() {
        return "유저 페이지 입니다.";
    }

    //로그인된 사용자가 admin일 경우 접속 가능
    @GetMapping("/admin")
    public String admin() {
        return "어드민 페이지 입니다.";
    }

    //http://localhost:8080/signup/?username=test&password=test&isAdmin=1 --> admin 으로 가입
    //http://localhost:8080/signup/?username=test1&password=test&isAdmin=0 --> user 으로 가입
    //위와 같이 username, password를 requestParam으로 선언해줘야 함.
    @GetMapping("/signup") //원래는 post써야함~!
    public String 회원가입(@RequestParam String username, @RequestParam String password, @RequestParam boolean isAdmin) {
        Set<Role> set = new HashSet<>();
        set.add(Role.ROLE_USER); // 회원가입이 되면 user권한 부여.

        if(isAdmin) set.add(Role.ROLE_ADMIN); //만약 isAdmin 값이 1일 경우 admin 권한이 부여됨.

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password)) //비밀번호 암호화
                .roles(set)
                .build();

        //db에 바로저장
        userRepository.saveAndFlush(user);

        //가입이 완료되었을 때 username을 불러와서 출력.
        return user.getUsername() + "님 가입 완료!";
    }

    //@AuthenticationPrincipal -> 세션 정보 UserDetails에 접근할 수 있는 어노테이션
    //user의 정보가 없으면 "인증 정보가 없어요." 가 출력. 아니면 username 출력.
    @GetMapping("/myinfo")
    public String myinfo(@AuthenticationPrincipal User user) {
        return (user != null) ? user.getUsername() : "인증 정보가 없어요.";
    }
}
