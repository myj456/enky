package com.english.enky.service;

import com.english.enky.dto.JoinDto;
import com.english.enky.entity.User;
import com.english.enky.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    public void joinProcess(JoinDto joinDto){
        String username = joinDto.getUsername();
        String password = joinDto.getPassword();
        String nickname = joinDto.getNickname();

        Boolean isExistUn = userRepository.existsByUsername(username);
        Boolean isExistNn = userRepository.existsByNickname(nickname);

        if (isExistUn){
            return;
        }
        if (isExistNn){
            return;
        }

        User user = new User();

        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password)); // 비밀번호 암호화
        user.setNickname(nickname);
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);
    }
}
