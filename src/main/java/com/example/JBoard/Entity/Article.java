package com.example.JBoard.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Article {

    @Id @GeneratedValue
    private Long ArticleId;

    private String title;

    private String content;
}
