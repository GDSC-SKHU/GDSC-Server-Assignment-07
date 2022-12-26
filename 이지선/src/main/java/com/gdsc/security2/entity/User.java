package com.gdsc.security2.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name="user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User implements UserDetails {      // UserDetails는 Spring Security에서 사용자의 정보를 담는 인터페이스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name="password")
    private String password;

    // RDB는 컬렉션과 같은 형태의 데이터를 컬럼에 저장할 수 없기 때문에, 별도의 테이블을 생성하여 컬렉션을 관리
    @ElementCollection(fetch = FetchType.EAGER)     // 컬렉션 객체임을 알려줌
    // EnumType.STRING: enum 이름을 DB에 저장 | EnumType.ORDINAL: enum의 순서값을 DB에 저장
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    // 권한 목록 세팅
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(x -> new SimpleGrantedAuthority(x.name()))
                .collect(Collectors.toList());
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    // 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    // 사용자 활성화 여부
    @Override
    public boolean isEnabled() {
        return false;
    }
}
