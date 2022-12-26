package com.gdsc.security2.config;

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
        http.csrf().disable();          // csrf 비활성화, 권장되는 방법X
        return http
                .authorizeRequests()                                   // Request에 인증,인가 부여 (보안검사 시작)
                .antMatchers("/user/**").hasRole("USER")    // /user로 시작하는 uri는 USER 권한이 있어야 접속가능
                .antMatchers("/admin/**").hasRole("ADMIN")  // /admin으로 시작하는 uri는 ADMIN 권한이 있어야 접속가능
                .anyRequest().permitAll()                              // 그 외에는 인증되지 않은 유저도 접속 가능
                .and()
                .formLogin()                                           // 폼 로그인 방식
                .loginProcessingUrl("/login")                          // 로그인 uri
                .defaultSuccessUrl("/")                                // 로그인 성공시 리다이렉트되는 uri
                .and().build();
    }
}