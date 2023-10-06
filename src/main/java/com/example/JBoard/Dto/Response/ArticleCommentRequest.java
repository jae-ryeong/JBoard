package com.example.JBoard.Dto.Response;

import com.example.JBoard.Dto.ArticleCommentDtoC;
import com.example.JBoard.Dto.UserAccountDto;

public record ArticleCommentRequest(
        Long articleId,
        String content
) {
    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDtoC toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDtoC.of(articleId, userAccountDto, content);
    }

}
