package com.example.gdsc07.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER) // 즉시 로딩으로 관리
    @Enumerated(value = EnumType.STRING) // DB에 enum의 값 자체가 텍스트 그대로 저장
    private Set<Role> roles; // 우리가 만든 role을 불러와야 함

    // 해당 사용자 계정의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(x -> new SimpleGrantedAuthority(x.name())).collect(Collectors.toList());
    } // x.name은 스트링 형태로 반환해 줌

    // 계정 비밀번호 리턴
    @Override
    public String getPassword() {
        return password;
    }

    // 계정 이름 리턴
    @Override
    public String getUsername() {
        return username;
    }

    // 계정이 만료되지 않았는지 리턴
    // true : 만료되지 않음
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않은지 리턴
    // true : 잠겨있지 않음
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되지 않았는지 리턴
    // true : 만료되지 않음
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 사용가능한 계정인지 리턴
    // true : 사용가능한 계정
    @Override
    public boolean isEnabled() {
        return true;
    }
}