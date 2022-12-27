package com.example.security2.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
// Table user를 지칭하는 어노테이션
@Table(name = "user")
@Getter

// NoArgsConstructor 어노테이션 : 파라미터가 없는 기본 생성자를 생성
// access = AccessLevel.? : 생성자 접근 Level을 다음과 같은 설정값으로 줄 수 있다
// AccessLevel.PROTECTED : 다른 패키지에 소속된 클래스 (상속 제외)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// AllArgsConstructor 어노테이션 : '모든 매개변수 생성자'인 것 처럼 해당 클래스 내의 모든 변수값을 가진 생성자를 자동으로 만들어줌
@AllArgsConstructor
@Builder
// implements : 부모의 메소드를 반드시 오버라이딩(재정의)해야 한다. 다중상속을 대신해준다.
public class User implements UserDetails {
    @Id
    // GeneratedValue(strategy = GenerationType.IDENTITY) : 기본 키 생성을 데이터베이스에 위임
    // 즉, id 값을 null로 하면 DB가 알아서 AUTO_INCREMENT 해준다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    // 권한 가져오는
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(x -> new SimpleGrantedAuthority(x.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
