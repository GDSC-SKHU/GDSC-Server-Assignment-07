package com.example.security2.entity;


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
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;//list랑 비슷함 중복이 없는 list

    // 사용자의 권한을 콜렉션 형태로 반환
    // 단, 클래스 자료형은 GrantedAuthority를 구현해야함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(x -> //내가 가지고 있는 롤을 스트림하겠다.
                new SimpleGrantedAuthority(x.name())).collect(Collectors.toList());//롤 어드민을 스트림 형태로 반환
    }
    /*
    getAuthorities()인데,
    이 메소드는 사용자의 권한을 콜렉션 형태로 반환해야하고,
    콜렉션의 자료형은 무조건적으로 GrantedAuthority를 구현해야한다.
     */

    //계정 만료 여부 반환 (true = 만료되지 않음을 의미)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠금 여부 반환 (true = 잠금되지 않음을 의미)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //패스워드 만료 여부 반환 (true = 만료되지 않음을 의미)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 사용 가능 여부 반환 (true = 사용 가능을 의미)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
