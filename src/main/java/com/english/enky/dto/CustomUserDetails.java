package com.english.enky.dto;

import com.english.enky.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


// Security가 User엔티티를 이해할 수 있는 형태로 변환해주는 사용자 정보 래퍼 클래스
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    // Security 인가에서 사용하기 위해 상요자의 권한 목록을 반환함.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        // role을 grantedAuthority(Security 권한 표현 인터페이스)로 변환
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collection;
    }

    // Security가 입력받은 값과 비교하기 위해서 사용함.
    // 비밀번호 반환
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 이름
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화
    @Override
    public boolean isEnabled() {
        return true;
    }
}
