package com.example.JBoard.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    private String title;

    @Column(length = 10000)
    private String content;

    private Long view_count;  // 조회수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserAccount userAccount;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) //  CascadeType.PERSIST + CascadeType.REMOVE
    private Set<ArticleComment> articleComment;

    private Article(UserAccount userAccount, String title, String content, Long view_count){
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.view_count = view_count;
    }

    @Override
    public String toString() {
        return "Article{" +
                "ArticleId=" + articleId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", view_count=" + view_count +
                ", user=" + userAccount +
                '}';
    }

    public static Article of(UserAccount userAccount, String title, String content, Long view_count) {
        return new Article(userAccount, title, content, view_count);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addView() {
        this.view_count++;
    }
}
