package com.english.enky.jwt;

import com.english.enky.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

// 로그인
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("=== Login Attempt ===");

        // 클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("Username: " + username);
        System.out.println("Password: " + (password != null ? "[PROVIDED]" : "[NULL]"));

        // 스프링 시큐리티에서 username, password를 검증하기 위해서는 token에 담아야함.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // token에 담은 검증을 위한 AuthenticationManager
        return authenticationManager.authenticate(authenticationToken);
    }

    // 로그인 성공 (jwt 토큰 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication){
        System.out.println("=== Login Successful ===");

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal(); // 유저 정보 가져오기

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        
        String role = auth.getAuthority();

        System.out.println("Generating token for - Username: " + username + ", Role: " + role);

        // 토큰 발급
        String token = jwtUtil.createJWT(username, role, 60*60*10*1000L);

        System.out.println("Generated Token (first 50 chars): " + token.substring(0, Math.min(token.length(), 50)) + "...");

        // RFC 7235 정의 형태에 따라서 HTTP 헤더 인증
        response.addHeader("Authorization", "Bearer " + token);

        System.out.println("Token added to Authorization header");
    }

    // 로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        System.out.println("=== Login Failed ===");
        System.out.println("Failure reason: " + failed.getMessage());

        response.setStatus(401);
    }

    // => 로그인 성공/실패 메서드는 securityConfig에서 등록해줘야됨.
}
