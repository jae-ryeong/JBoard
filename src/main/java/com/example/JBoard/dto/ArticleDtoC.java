package com.example.JBoard.dto;

import com.example.JBoard.Entity.Article;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ArticleDtoC {
    private String title;
    private String content;

    public ArticleDtoC(String title, String content){
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "ArticleDtoC{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    // DTO -> Entity
    public Article toEntity() {
        return Article.of(title, content, 0L);
    }

    public static ArticleDtoC from(Article entity) {
        return new ArticleDtoC(
                entity.getTitle(),
                entity.getContent()
        );
    }

    public static ArticleDtoC of(String title, String content){
        return new ArticleDtoC(title, content);
    }
}
