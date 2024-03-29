package com.example.JBoard.Dto.Response;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.Article;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long articleId,
        String title,
        String content,
        UserAccountDto userAccountDto,
        LocalDateTime createdAt,
        Long view_count,
        Long fileId) {

    public static ArticleResponse of(Long articleId, String title, String content, UserAccountDto userAccountDto, Long fileId) {
        return new ArticleResponse(articleId, title, content, userAccountDto, null, null, fileId);
    }

    public static ArticleResponse from(ArticleDtoC dto) {
        return new ArticleResponse(dto.getArticleId(), dto.getTitle(), dto.getContent(), dto.getUserAccountDto(), dto.getCreateAt(), dto.getViewCount(), dto.getFileId());
    }
}
