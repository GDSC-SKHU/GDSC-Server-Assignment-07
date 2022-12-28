package com.example.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean //패스워드 암호화 따로 빈으로 등록하여 필요할 때마다 꺼내기
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //시큐리티 관련 설정을 빈으로 등록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // csrf 비활성화, (csrf공격=취약점 중 하나) 원래는 켜놔야 하지만 토큰 인증을 위해 꺼놈
        return http
                .authorizeRequests() // Request에 인증, 인가를 부여하겠다.
                .antMatchers("/user/**").hasRole("USER") // USER=ROLE_USER /user 로 시작하는 uri는 USER 롤이 있어야 접속가능
                .antMatchers("/admin/**").hasRole("ADMIN") // ADMIN=ROLE_ADMIN /admin으로 작하는 uri는 ADMIN 롤이 있어야 접속가능.
                .anyRequest().permitAll() // 그 외에는 인증되지 않은 유저도 접속 가능
                //.antMatchers("/test").authenticated() ??
                .and()
                .formLogin() // 폼 로그인을 사용하겠다.
                .loginProcessingUrl("/login") // 로그인 uri
                .defaultSuccessUrl("/") // 로그인 성공시 리다이렉트 uri
                .and().build();
    }
}
