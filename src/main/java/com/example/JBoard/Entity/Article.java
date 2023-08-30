package com.example.JBoard.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ArticleId;

    private String title;

    @Column(length = 10000)
    private String content;

    private int view_count;  // 조회수

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}
