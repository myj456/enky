package com.english.enky.service;

import com.english.enky.dto.CustomUserDetails;
import com.english.enky.entity.User;
import com.english.enky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 로그인한 사용자 정보를 조회 서비스
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        // username을 통해서 유저 정보를 조회함.
        User user = userRepository.findByUsername(username);

        // 사용자 존재 여부 확인 후 정보 반환
        if(user != null){
            return new CustomUserDetails(user);
        }

        return null;
    }


}
