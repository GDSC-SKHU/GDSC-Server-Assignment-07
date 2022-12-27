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

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index(){
        return "메인 페이지 입니다.";
    }

    @GetMapping("/user")
    public String user(){
        return "유저 페이지 입니다.";
    }

    @GetMapping("/admin")
        public String admin(){
            return "어드민 페이지 입니다.";
        }

    @GetMapping("/signup")
    public String 회원가입(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam boolean isAdmin) {

        Set<Role> set = new HashSet<>();
        set.add(Role.ROLE_USER);//ROLE_USER 추가
        /*
        HashSet의 특징 :

        1. 중복된 값을 허용하지 않음
        2. 입력한 순서가 보장되지 않음
        3. null을 값으로 허용
         */

        if (isAdmin) set.add(Role.ROLE_ADMIN);

        User user = User.builder()//user 빌드
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(set)
                .build();

        userRepository.saveAndFlush(user);

        return user.getUsername()+ "님 가입 완료";
    }

    @GetMapping("/myinfo")
    public String myInfo(@AuthenticationPrincipal User user) {
        return (user == null) ? "인증 정보가 없어요" : user.getAuthorities().toString();
    }
    /*   @AuthenticationPrincipal :
         인증이후 편의적으로 현재 인증된 세션유저를 가져오기 위해
         @AuthenticationPrincipal 어노테이션을 통해 UserDetails 인터페이스를 구현한 유저 객체를
         주입할 때 사용하는 편이다.

        * @AuthenticationPrincipal 은 UserDetails 타입을 가지고 있음
        -> UserDetails 타입을 구현한 PrincipalDetails 클래스를 받아 User object를 얻는다

        * userDetails(PrincipalDetails 타입).getUser()
        따라서 로그인 세션 정보가 필요한 컨트롤러에서 매번
        @AuthenticationPrincipal로 세션 정보를 받아서 사용한다.

        * @AuthenticationPrincipal UserAdapter 타입
           -로그인 세션 정보를 애노테이션으로 간편하게 받을 수 있다
           -UserDetailsService에서 Return한 객체를 파라미터로 직접 받아 사용할 수 있다.
           -name 뿐만 아니라 다양한 정보를 받을 수 있다
           -중복 코드를 효율적으로 줄일 수 있다.
     */
}
