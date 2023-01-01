package com.example.security2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean// BCryptPasswordEncoder 빈 등록
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean//SecurityFilterChain 빈등록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();//http의 csrf를 disable한다. 모든 요청을 받는다. <- 보안에 위험 , csrf 토큰이랑 같이 보내야 한다.
        //필터 반환
        return http
                .authorizeRequests()
                .antMatchers("/user/**").hasRole("USER")// user/** 아래의 파일은 USER만 접근 가능
                .antMatchers("/admin/**").hasRole("ADMIN") // admin/** 아래 파일은 ADMIN만 접근 가능
                //.antMatchers("/test").authenticated()
                .anyRequest().permitAll()//그 외 요청은 모두 허락한다.
                .and()
                .formLogin()//폼 로그인을 사용하겠다.
                .loginProcessingUrl("/login")// 로그인 uri
                .defaultSuccessUrl("/")//로그인 성공시 리다이렉트 uri
                .and()
                .build();
    }
}
