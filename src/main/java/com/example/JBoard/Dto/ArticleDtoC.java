package com.example.JBoard.Dto;

import com.example.JBoard.Entity.Article;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArticleDtoC {
    private final Long ArticleId;
    private String title;
    private String content;

    public ArticleDtoC(Long articleId, String title, String content){
        this.ArticleId = articleId;
        this.title = title;
        this.content = content;
    }

    // DTO -> Entity
    public Article toEntity() {
        return Article.of(title, content, 0L);
    }
}
