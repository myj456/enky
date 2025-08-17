package com.english.enky.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("스펠링")
    @Column(name = "spelling")
    private String spelling;

    @Comment("뜻")
    @Column(name = "mean")
    private String mean;

    @Comment("생성일")
    @Column(name = "created_at")
    private LocalDateTime created;

    @Comment("수정일")
    @Column(name = "updated_at")
    private LocalDateTime updated;

//    @Column()
//    private User userid;


}
