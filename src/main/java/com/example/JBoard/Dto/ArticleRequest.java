package com.example.JBoard.Dto;

import lombok.Data;

public record ArticleRequest(
        String title,
        String content
) {
    public ArticleRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
