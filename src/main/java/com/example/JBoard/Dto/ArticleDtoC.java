package com.example.JBoard.Dto;

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

    public static ArticleDtoC of(String title, String content){
        return new ArticleDtoC(title, content);
    }
}
