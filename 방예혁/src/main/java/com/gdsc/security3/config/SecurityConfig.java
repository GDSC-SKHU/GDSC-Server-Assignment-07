package com.gdsc.security3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Configuration 역할을 하는 Class 지정 가능
public class SecurityConfig {

    @Bean // Bean: Spring IoC 컨테이너가 관리하는 자바 객체
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // csrf 비활성화, 권장되는 방법은 아님.
        return http
                .authorizeRequests() // Request에 인증, 인가를 부여하겠다.
                .antMatchers("/user/**").hasRole("USER") // "/user/**" 형식의 uri 요청에 대해 인증을 요구, USER 권한이 있으면 접속 가능
                .antMatchers("/admin/**").hasRole("ADMIN") // "/admin/**" 형식의 uri 요청에 대해 인증을 요구, ADMIN 권한이 있으면 접속 가능
                .anyRequest().permitAll() // 그 외의 요청은 인증되지 않은 유저도 접속 가능
                .and()
                .formLogin() // 폼 로그인을 사용하겠다.
                .loginProcessingUrl("/login") // 로그인 form action uri
                .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트 uri
                .and().build();
    }
}