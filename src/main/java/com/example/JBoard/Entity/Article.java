package com.example.JBoard.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Article {

    @Id @GeneratedValue
    private Long ArticleId;

    private String title;

    private String content;

    private int view_count;  // 조회수

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

}
