package com.example.JBoard.Dto;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;

import java.time.LocalDateTime;

public record ReplyDto(
        Long articleId,
        Long commentId,
        String content,
        LocalDateTime createdAt,
        UserAccountDto userAccountDto,
        Long parentOrder,
        ArticleComment parent) {

    public static ReplyDto of(Long articleId, Long commentId, String content, UserAccountDto userAccountDto) {
        return new ReplyDto(articleId, commentId, content, null, userAccountDto, null, null);
    }

    public ArticleComment toEntity(Article article, UserAccount userAccount, ArticleComment parent) {
        return ArticleComment.of(
                userAccount, article, content, parent
        );
    }

    public static ReplyDto from(ArticleComment articleComment) {
        return new ReplyDto(articleComment.getArticle().getArticleId(), articleComment.getCommentId(), articleComment.getContent(),
                articleComment.getCreatedAt(), UserAccountDto.from(articleComment.getUserAccount()), null, null); //TODO: 여기 다시 봐야한다.
    }
}
