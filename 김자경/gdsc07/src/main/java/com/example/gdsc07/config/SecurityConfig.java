package com.example.gdsc07.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// @Configuration + @Bean을 이용하여 Bean을 등록
// @Configuration을 이용하면 Spring Project에서의 Configuration역할을 하는 Class를 지정
@Configuration
public class SecurityConfig {

    // 비밀번호 암호화에 필요한 passwordEncoder를 Bean 등록
    // Bean : Spring IoC 컨테이너가 관리하는 자바 객체
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // filterChain을 Bean 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf 비활성화, 권장되는 방법은 아님.
        // 왠만하면 끄지 말자 대신 csrf 토큰이랑 같이 보내야 함
        http.csrf().disable();
        return http
                .authorizeRequests() // Request에 인증, 인가를 부여하겠다 ➜ 요청에 대한 사용권한 체크
                .antMatchers("/user/**").hasRole("USER") // /user 로 시작하는 uri는 USER 롤이 있어야 접속가능
                .antMatchers("/admin/**").hasRole("ADMIN") // /admin으로 작하는 uri는 ADMIN 롤이 있어야 접속가능
                .anyRequest().permitAll() // 그 외에는 인증되지 않은 유저도 접속 가능 ➜ 그 외 요청들은 인증, 인가가 필요없다
                .and()
                .formLogin() // 폼 로그인을 사용하겠다.
                .loginProcessingUrl("/login") // 로그인 uri
                .defaultSuccessUrl("/") // 로그인 성공시 리다이렉트 uri
                .and()
                .build();
    }
}