package com.example.JBoard.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ArticleComment extends AuditingFields{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @Column(length = 300, nullable = false)
    private String content;

    private ArticleComment(UserAccount userAccount, Article article, String content) {
        this.userAccount = userAccount;
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(UserAccount userAccount, Article article, String content) {
        return new ArticleComment(userAccount, article, content);
    }
}
