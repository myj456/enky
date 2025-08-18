package com.english.enky.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
@NoArgsConstructor // 기본 생성자
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;

    @Comment("아이디")
    @Column(name = "username")
    private String username;

    @Comment("비밀번호")
    @Column(name = "password")
    private String password;

    @Comment("닉네임")
    @Column(name = "nickname")
    private String nickname;

    @Comment("권한")
    @Column(name = "role")
    private String role;

    @Comment("생성일")
    @Column(name = "created_at")
    private LocalDateTime created;

    @Comment("수정일")
    @Column(name = "update_at")
    private LocalDateTime updated;
}
