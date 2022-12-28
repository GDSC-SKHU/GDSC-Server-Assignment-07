package com.example.security1.service;

import com.example.security1.domain.repository.UserRepository;
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

    //username을 UserDetail로 반환하겠다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Optional 형태이기때문에 유저네임이 null타입인지 검증해야한다
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 username이 없음"));
    }
}