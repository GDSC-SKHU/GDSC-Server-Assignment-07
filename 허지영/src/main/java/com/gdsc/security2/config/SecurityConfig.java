package com.gdsc.security2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // 보안에 위험

        return http.authorizeRequests()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll() // 그 외 요청은 인증, 인가 필요 x
                .and()
                .formLogin() // 폼 로그인 사용
                .loginProcessingUrl("/login") // 로그인 url
                .defaultSuccessUrl("/") // 로그인 성공시 리다이렉트 url
                .and()
                .build();

    }
}
