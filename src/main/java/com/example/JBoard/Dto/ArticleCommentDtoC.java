package com.example.JBoard.Dto;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ArticleCommentDtoC {

    private Long id;
    private Long articleId;
    private UserAccountDto userAccountDto;
    private String content;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public ArticleCommentDtoC(Long id, Long articleId, UserAccountDto userAccountDto, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        this.id = id;
        this.articleId = articleId;
        this.userAccountDto = userAccountDto;
        this.content = content;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }

    public static ArticleCommentDtoC of(Long articleId, UserAccountDto userAccountDto, String content) {
        return new ArticleCommentDtoC(null, articleId, userAccountDto, content, null, null, null, null);
    }

    public ArticleComment toEntity(Article article, UserAccount userAccount) {
        return ArticleComment.of(
                userAccount, article, content
        );
    }
}
