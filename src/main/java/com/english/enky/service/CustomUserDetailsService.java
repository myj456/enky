package com.english.enky.service;

import com.english.enky.dto.CustomUserDetails;
import com.english.enky.entity.User;
import com.english.enky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        // 유저 데이터 가져오기
        User user = userRepository.findByUsername(username);

        if(user != null){
            return new CustomUserDetails(user);
        }

        return null;
    }


}
