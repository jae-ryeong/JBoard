package com.example.JBoard.Dto.Response;

import com.example.JBoard.Dto.ArticleCommentDtoC;
import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.Request.ArticleCommentRequest;
import com.example.JBoard.Dto.UserAccountDto;

import java.time.LocalDateTime;

public record ArticleCommentResponse(
        Long articleId,
        String content,
        String nickname,
        LocalDateTime createdBy
) {
    public static ArticleCommentResponse of(Long articleId, String content, String nickname, LocalDateTime createdBy) {
        return new ArticleCommentResponse(articleId, content, nickname, createdBy);
    }

    /*public static ArticleCommentResponse from(ArticleDtoC articleDto) {
        return new ArticleCommentResponse(articleDto.get)
    }*/
}
