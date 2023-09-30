package com.example.JBoard.Dto;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.UserAccount;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class ArticleDtoC {
    private String title;
    private String content;
    private LocalDateTime createAt;
    private UserAccountDto userAccountDto;

    public ArticleDtoC(UserAccountDto userAccountDto, String title, String content, LocalDateTime createAt){
        this.userAccountDto = userAccountDto;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
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
        return Article.of(userAccount, title, content, 0L);
    }

    public static ArticleDtoC from(Article entity) {
        return new ArticleDtoC(
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getCreatedAt()
        );
    }

    public static ArticleDtoC of(UserAccountDto userAccountDto, String title, String content, LocalDateTime createAt){
        return new ArticleDtoC(userAccountDto, title, content, createAt);
    }

    public static ArticleDtoC of(UserAccountDto userAccountDto, String title, String content){
        return new ArticleDtoC(userAccountDto, title, content, null);
    }
}
