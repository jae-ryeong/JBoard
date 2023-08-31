package com.example.JBoard.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ArticleId;

    private String title;

    @Column(length = 10000)
    private String content;

    private Long view_count;  // 조회수

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private Article(String title, String content, Long view_count){
        this.title = title;
        this.content = content;
        this.view_count = view_count;
    }

    public static Article of(String title, String content, Long view_count) {
        return new Article(title, content, view_count);
    }
}
