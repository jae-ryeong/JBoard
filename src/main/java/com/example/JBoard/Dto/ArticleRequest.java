package com.example.JBoard.Dto;

public record ArticleRequest(
        String title,
        String content
) {
    public ArticleRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
