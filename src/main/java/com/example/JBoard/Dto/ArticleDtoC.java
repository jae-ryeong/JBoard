package com.example.JBoard.Dto;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.UserAccount;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class ArticleDtoC {
    private Long articleId;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private UserAccountDto userAccountDto;
    private Long viewCount;

    public ArticleDtoC(Long articleId, UserAccountDto userAccountDto, String title, String content, LocalDateTime createAt, Long viewCount){
        this.articleId = articleId;
        this.userAccountDto = userAccountDto;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "ArticleDtoC{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    // DTO -> Entity
    public Article toEntity(UserAccount userAccount) {
        return Article.of(userAccount, title, content.replace("\r\n","<br>"), 0L);
    }

    public static ArticleDtoC from(Article entity) {
        return new ArticleDtoC(
                entity.getArticleId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getView_count()
        );
    }

    public static ArticleDtoC of(UserAccountDto userAccountDto, String title, String content, LocalDateTime createAt){
        return new ArticleDtoC(null, userAccountDto, title, content, createAt, null);
    }

    public static ArticleDtoC of(UserAccountDto userAccountDto, String title, String content){
        return new ArticleDtoC(null, userAccountDto, title, content, null, null);
    }

    public static ArticleDtoC of(Long articleId, UserAccountDto userAccountDto, String title, String content, Long viewCount){
        return new ArticleDtoC(articleId, userAccountDto, title, content, null, viewCount);
    }
}
