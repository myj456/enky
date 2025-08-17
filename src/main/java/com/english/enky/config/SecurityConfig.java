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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    // 비밀번호 암호화 (Bcrypt 방식)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // csrf 비활성화
        http
                .csrf(AbstractHttpConfigurer::disable) // csrf
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 방식
                .httpBasic(AbstractHttpConfigurer::disable) // http basic 인증 방식
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/join").permitAll() // 경로 인가 적용
                        .requestMatchers("/admin").hasAnyRole("ADMIN") // 해당 경로에 USER 권한을 가진 유저만 접근 가능.
                        .anyRequest().authenticated());

        // 세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // jwt에선 session을 STATELESS로 관리함.

        // 메서드 등록
        http
                // LoginFilter 등록
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class)
                // JwtFilter 등록
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
                                // 허용할 헤더 내용 설정
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                // 허용되는 시간 설정
                                configuration.setMaxAge(3600L);

                                // 프론트가 헤더를 보낼 허용할 헤더 내용
                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        })));

        return http.build();
    }
}
