package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // csrf 비활성화, 권장되는 방법은 아님.
        return http
                .authorizeRequests() // Request에 인증, 인가를 부여하겠다.
                .antMatchers("/user/**").hasRole("USER") // /user 로 시작하는 uri는 USER 롤이 있어야 접속가능
                .antMatchers("/admin/**").hasRole("ADMIN") // /admin으로 작하는 uri는 ADMIN 롤이 있어야 접속가능.
                .anyRequest().permitAll() // 그 외에는 인증되지 않은 유저도 접속 가능
                .and()
                .formLogin() // 폼 로그인을 사용하겠다.
                .loginProcessingUrl("/login") // 로그인 uri
                .defaultSuccessUrl("/") // 로그인 성공시 리다이렉트 uri
                .and().build();
    }
}
