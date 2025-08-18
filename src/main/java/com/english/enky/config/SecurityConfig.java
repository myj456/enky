package com.english.enky.config;

import com.english.enky.jwt.JwtFilter;
import com.english.enky.jwt.JwtUtil;
import com.english.enky.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

/*
* Security, jwt 보안 설정
* */

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    // 사용자 인증 처리 컴포넌트 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    // 비밀번호 암호화 (Bcrypt 방식)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /* filter Chanin 설정
    HTTP 요청에 대한 보안 정책과 인증/인가 규칙을 정의
    * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // HTTP 요청 설정
        http
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화 => session statless이기 때문. 인증 정보를 보관 X
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 방식
                .httpBasic(AbstractHttpConfigurer::disable) // http basic 인증 방식
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/join").permitAll() // 경로 인가 적용
                        .requestMatchers("/admin").hasAnyRole("ADMIN") // 해당 경로에 USER 권한을 가진 유저만 접근 가능.
                        .anyRequest().authenticated());

        // 세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // jwt에선 session을 STATELESS로 관리함. => 토큰에 인증 정보가 모두 담겨 있기 때문.

        // 커스텀 필터 등록
        // 모든 요청을 jwt로 확인하기 위해서.
        http
                // LoginFilter 등록
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class)
                // LoginFilter 이전에 JwtFilter 등록
                // 필터 순서 : jwt -> login -> security
                // addFilterBefore은 특정 필터 앞에 새로운 필터를 추가하는 메서드임.
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        // cors 설정
        http
                .cors((corsCustomizer -> corsCustomizer
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();

                                // 허용할 서버 설정
                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                // Http Method 허용 설정
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                // Credential (프론트) 허용 여부
                                configuration.setAllowCredentials(true);
                                // 응답 헤더 설정
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                // 허용되는 시간 설정
                                configuration.setMaxAge(3600L);

                                // 클라이언트가 접근할 수 있는 응답 헤더 설정
                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        })));

        return http.build();
    }
}
