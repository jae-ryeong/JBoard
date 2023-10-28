package com.example.JBoard.Dto;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;

import java.time.LocalDateTime;

public record ReplyDto(
        Long id,
        Long articleId,
        Long commentId,
        String content,
        LocalDateTime createdAt,
        UserAccountDto userAccountDto,
        Long parentOrder,
        ArticleComment parent) {

    public static ReplyDto of(Long articleId, Long commentId, String content, UserAccountDto userAccountDto) {
        return new ReplyDto(null, articleId, commentId, content, null, userAccountDto, null, null);
    }

    public ArticleComment toEntity(Article article, UserAccount userAccount, ArticleComment parent) {
        return ArticleComment.of(
                userAccount, article, content, parent.getParentOrder(), parent
        );
    }
}
