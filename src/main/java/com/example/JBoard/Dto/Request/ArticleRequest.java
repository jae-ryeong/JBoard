package com.example.JBoard.Dto.Request;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.UserAccountDto;

public record ArticleRequest(
        String title,
        String content
) {
    public ArticleRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public ArticleDtoC toDto(UserAccountDto userAccountDto){
        return ArticleDtoC.of(userAccountDto, title, content);
    }

}
