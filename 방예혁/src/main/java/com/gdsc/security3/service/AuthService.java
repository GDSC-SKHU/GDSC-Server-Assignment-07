package com.gdsc.security3.service;

import com.gdsc.security3.domain.repository.UserRepository;
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
    // 클래스 내부에 UserRepository를 주입받아 DB와 연결하여 처리함
    private final UserRepository userRepository;

    // 찾는 username이 없으면 UsernameNotFoundException 발생
    // 있으면 UserDetails 리턴
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 username이 없음"));
    }
}