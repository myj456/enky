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

    // 회원가입 처리
    public void joinProcess(JoinDto joinDto){
        String username = joinDto.getUsername();
        String password = joinDto.getPassword();
        String nickname = joinDto.getNickname();

        // 중복 검사
        Boolean isExistUn = userRepository.existsByUsername(username);
        Boolean isExistNn = userRepository.existsByNickname(nickname);

        if (isExistUn){
            return;
        }
        if (isExistNn){
            return;
        }

        // 새로운 사용자 엔티티 생성
        User user = new User();

        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password)); // 비밀번호 암호화
        user.setNickname(nickname);
        user.setRole("ROLE_ADMIN");

        // 사용자 정보 저장
        userRepository.save(user);
    }
}
