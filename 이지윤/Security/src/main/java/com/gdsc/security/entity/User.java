package com.gdsc.security.entity;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
//상속법 or 따로 뺼건지 결정
//UserDetails -> 사용자 정보(username, password 등)을 가지는 인터페이스
//UserDetails에서의 GrantedAuthority는 현재 사용자(Principal)가 가지고 있는 권한을 말함.
public class User implements UserDetails {

    //id, username, password 컬럼 생성
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;


    //컬렉션 객체임을 jpa에 알려주는 어노테이션 (basic type)
    @ElementCollection(fetch = FetchType.EAGER)
    //enum 이름을 DB에 저장함.
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    //인가 관련 정의 함수
    //유저의 권한 목록, 권한을 반환.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(x -> new SimpleGrantedAuthority(x.name()))
                .collect(Collectors.toList());
    }

    //계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
