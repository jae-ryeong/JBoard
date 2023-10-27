package com.example.JBoard.Dto;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleCommentDtoC {
    private Long id;
    private Long articleId;
    private String content;
    private LocalDateTime createdAt;
    private UserAccountDto userAccountDto;

    public ArticleCommentDtoC(Long id, Long articleId, String content, LocalDateTime createdAt, UserAccountDto userAccountDto) {
        this.id = id;
        this.articleId = articleId;
        this.content = content;
        this.createdAt = createdAt;
        this.userAccountDto = userAccountDto;
    }

    public static ArticleCommentDtoC of(Long articleId, String content, UserAccountDto userAccountDto) {
        return new ArticleCommentDtoC(null, articleId, content, null, userAccountDto);
    }

    public ArticleComment toEntity(Article article, UserAccount userAccount, Long parentOrder) {
        return ArticleComment.of(
                userAccount, article, content, parentOrder);
    }

    public static ArticleCommentDtoC from(ArticleComment articleComment) {
        return new ArticleCommentDtoC(articleComment.getCommentId(), articleComment.getArticle().getArticleId()
                ,articleComment.getContent(), articleComment.getCreatedAt(), UserAccountDto.from(articleComment.getUserAccount()));
    }
}
