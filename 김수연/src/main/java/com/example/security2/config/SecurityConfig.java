package com.example.security2.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        /*
        BCryptPasswordEncoder:
        스프링 시큐리티(Spring Seurity) 프레임워크에서 제공하는 클래스 중 하나로
        비밀번호를 암호화하는 데 사용할 수 있는 메서드를 가진 클래스
        */
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // csrf 비활성화
        /* CSRF:
        Cross site Request forgery로 사이즈간 위조 요청인데,
        즉 정상적인 사용자가 의도치 않은 위조요청을 보내는 것을 의미한다.

        예를 들어 A라는 도메인에서,
        인증된 사용자 H가 위조된 request를 포함한 link,
        email을 사용하였을 경우(클릭, 또는 사이트 방문만으로도),
        A 도메인에서는 이 사용자가 일반 유저인지, 악용된 공격인지 구분할 수가 없다.
       */
        return http
                .authorizeRequests() // Request에 인증, 인가를 부여하겠다.
                .antMatchers("/user/**").hasRole("USER") // /user 로 시작하는 uri는 USER 롤이 있어야 접속가능
                // 해당 request가 오면, "USER, ADMIN"만 접근 가능하도록 설정

                .antMatchers("/admin/**").hasRole("ADMIN") // /admin으로 시작하는 uri는 ADMIN 롤이 있어야 접속가능.
                // 해당 request가 오면, "ADMIN"만 접근 가능하도록 설정

                .anyRequest().permitAll() // 그 외의 요청들은 인증, 인가가 필요없음
                .and()
                .formLogin() // 폼 로그인을 사용하겠다.
                .loginProcessingUrl("/login") // 로그인 uri
                .defaultSuccessUrl("/") // 로그인 성공시 리다이렉트 uri
                .and().build();
    }
}
/*
    antMatchers : 특정 리소스에 대해서 권한을 설정
    permitAll : antMatchers에서 설정한 리소스의 접근을 인증절차 없이 허용
    hasRole : 리소스에 대해서 특정 권한을 가진 사용자만 접근 허용
    authenticated : 인증된 사용자의 접근을 허용
 */