package com.english.enky.jwt;


import com.english.enky.dto.CustomUserDetails;
import com.english.enky.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    // jwt 토큰 인증
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        System.out.println("=== JWT Filter Start ===");
//        System.out.println("Request URI: " + request.getRequestURI());
//        System.out.println("Request Method: " + request.getMethod());


        // HTTP 요청 헤더에서 Authorization 헤더 값을 가져옴.
        String authorization = request.getHeader("Authorization");

        // 헤더 값 여부 및 헤더 RFC7235인증 형태가 맞는지 확인
        if(authorization == null || !authorization.startsWith("Bearer ")){
            System.out.println("token null");

            filterChain.doFilter(request, response);
            return;
        }

        // token 값만 받음.
        String token = authorization.split(" ")[1];

        // 토큰 만료일 확인
        if(jwtUtil.isExpired(token)){
            System.out.println("token expired");

            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

//        System.out.println("Token Valid - Username: " + username + ", Role: " + role);

        // 임시 User 객체 생성 -> jwt 토큰 정보만으로 인증 객체 생성
        User user = new User();
        user.setUsername(username);
        user.setPassword("temppassword");
        user.setRole(role);

        // Security가 사용할 userDetails 객체 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // principal: 인증된 사용자 객체, Credentials: 자격 증명 정보(토큰), Authorities: 사용자 권한 목록
        // Security 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // securitycontextholder에 authToken을 전달하여 security 사용자를 등록함.
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
