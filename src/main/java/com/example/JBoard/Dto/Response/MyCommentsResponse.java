package com.example.JBoard.Dto.Response;

import com.example.JBoard.Entity.ArticleComment;

import java.time.LocalDateTime;

public record MyCommentsResponse(
        Long commentId,
        Long articleId,
        String content,
        String nickname,
        LocalDateTime createdBy
) {
    public static MyCommentsResponse of(Long commentId, Long id, Long articleId, String content, String nickname, LocalDateTime createdBy) {
        return new MyCommentsResponse(commentId, articleId, content, nickname, createdBy);
    }

    public static MyCommentsResponse from(ArticleComment articleComment) {
        return new MyCommentsResponse(articleComment.getCommentId(), articleComment.getArticle().getArticleId(),
                articleComment.getContent(), articleComment.getUserAccount().getNickname(), articleComment.getCreatedAt());
    }
}
