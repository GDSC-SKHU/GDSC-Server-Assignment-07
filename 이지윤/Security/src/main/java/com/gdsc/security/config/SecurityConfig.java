package com.gdsc.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    //pw 암호화 빈 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //web 취약점 공격자가 의도한 행위를 요청하게 만드는 공격임. 원래는 켜놔야함. 우리 공격당해!!!
        http.csrf().disable(); // 토큰 인증시 끔.

        return http.authorizeHttpRequests()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll() //그 외 요청들은 인증, 인가 불필요
//                .antMatchers("/test").authenticated() //인증 필요, 인가 불필요
                .and()
                .formLogin()//폼 로그인 받음.
                .loginProcessingUrl("/login")//로그인 uri
                .defaultSuccessUrl("/")//로그인 성공시 리다이렉트 uri
                .and()
                .build();
    }
}
