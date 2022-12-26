package com.gdsc.security3.domain.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/*
   security database
   user table
   id: primary key(기본키)
   username: unique(유일한 값만 가능), 사용자명
   password: 비밀번호
*/

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User implements UserDetails { // UserDetails: 사용자의 정보를 담는 인터페이스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING) // Role(enum)에 있는 값들이 문자열 그대로 저장됨(EnumType.STRING)
    private Set<Role> roles;

    // 계정의 권한 목록을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(x -> new SimpleGrantedAuthority(x.name()))
                .collect(Collectors.toList());
    }

    // 계정의 비밀번호를 리턴
    @Override
    public String getPassword() {
        return password;
    }

    // 계정의 username 리턴
    @Override
    public String getUsername() {
        return username;
    }

    // 계정의 만료 여부 리턴
    @Override
    public boolean isAccountNonExpired() {
        return true; // 만료 안됨
    }

    // 계정의 잠김 여부 리턴
    @Override
    public boolean isAccountNonLocked() {
        return true; // 잠기지 않음
    }

    // 비밀번호 만료 여부 리턴
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 만료 안됨
    }

    // 계정의 활성화 여부 리턴
    @Override
    public boolean isEnabled() {
        return true; // 활성화 됨
    }
}