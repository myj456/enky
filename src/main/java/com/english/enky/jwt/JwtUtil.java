package com.english.enky.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// jwt 토큰의 전체 생명주기를 관리
@Component
public class JwtUtil {

    private SecretKey secretKey;

    // jwt 키를 불러옴.
    public JwtUtil(@Value("${spring.jwt.secret}") String secret){
        // 키를 객체 타입으로 저장하여 초기화(HS256)를 함. => 암호화하기 위해서.
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // username 검증
    public String getUsername(String token){
        // jwt 키가 여기 서버에서 만들어진게 맞는지 검증(verifyWith 메서드)함.
        // parseSignedClaims 확인 하고 username String타입 값을 가져옴.
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().get("username", String.class);
    }

    // role 검증
    public String getRole(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().get("role", String.class);
    }

    // 토큰 시간이 만료되었는지 확인
    public Boolean isExpired(String token){
        // getExpiration().before()를 통해 내부에 시간값을 넣으면 알 수 있음.
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
    }

    // 토큰 생성
    public String createJWT(String username, String role, Long expiredMS){
        return Jwts.builder()
                // claim을 통해 특정값을 넣을 수 있음.
                .claim("username", username)
                .claim("role", role)
                // 발행 시간
                .issuedAt(new Date(System.currentTimeMillis()))
                // 만료 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMS))
                .signWith(secretKey) // 암호화
                .compact();
    }



}
