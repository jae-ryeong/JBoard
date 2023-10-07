package com.example.JBoard.Dto;

import com.example.JBoard.Entity.ArticleComment;

import java.time.LocalDateTime;

public record ArticleCommentDto(
        Long id,
        Long articleId,
        String content,
        LocalDateTime createdAt,
        String nickname
) {
    public static ArticleCommentDto of(Long articleId, String content) {
        return new ArticleCommentDto(null, articleId, content,null,null);
    }

    public static ArticleCommentDto from(ArticleComment articleComment) {
        return new ArticleCommentDto(articleComment.getId(), articleComment.getArticle().getArticleId(),
                articleComment.getContent(), articleComment.getCreatedAt(), articleComment.getUserAccount().getNickname());
    }
}
