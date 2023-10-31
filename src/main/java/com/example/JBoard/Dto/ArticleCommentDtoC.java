package com.example.JBoard.Dto;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ArticleCommentDtoC {
    private Long id;
    private Long articleId;
    private String content;
    private LocalDateTime createdAt;
    private UserAccountDto userAccountDto;
    private Set<ArticleCommentDtoC> children;

    public ArticleCommentDtoC(Long id, Long articleId, String content, LocalDateTime createdAt, UserAccountDto userAccountDto, Set<ArticleCommentDtoC> children) {
        this.id = id;
        this.articleId = articleId;
        this.content = content;
        this.createdAt = createdAt;
        this.userAccountDto = userAccountDto;
        this.children = children;
    }

    public static ArticleCommentDtoC of(Long articleId, String content, UserAccountDto userAccountDto) {
        return new ArticleCommentDtoC(null, articleId, content, null, userAccountDto, null);
    }

    public ArticleComment toEntity(Article article, UserAccount userAccount) {
        return ArticleComment.of(
                userAccount, article, content);
    }

    public static ArticleCommentDtoC from(ArticleComment articleComment) {
        return new ArticleCommentDtoC(articleComment.getCommentId(), articleComment.getArticle().getArticleId()
                ,articleComment.getContent(), articleComment.getCreatedAt(), UserAccountDto.from(articleComment.getUserAccount()),
                articleComment.getChildren().stream()
                        .map(ArticleCommentDtoC::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
    }
}
