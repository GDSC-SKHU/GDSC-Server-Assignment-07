package com.example.gdsc07.service;

import com.example.gdsc07.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    // DB에서 유저정보를 불러옴
    // Custom한 UserDetails 클래스를 리턴
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // userRepository에서 사용자를 찾는 findByUsername 메소드 사용
        // 없으면 NOT_FOUND 오류 발생
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 username이 없음"));
    }
}
