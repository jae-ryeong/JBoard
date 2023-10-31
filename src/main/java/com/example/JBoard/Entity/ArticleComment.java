package com.example.JBoard.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class ArticleComment extends AuditingFields{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_Id")
    private Long commentId;

    @ManyToOne
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @Column(length = 300, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id", referencedColumnName = "comment_Id")
    private ArticleComment parent;  // 대댓글이 가질 부모 댓글

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)  // 부모 댓글 삭제시 자식 모두 삭제
    @OrderBy("commentId asc")
    private Set<ArticleComment> children = new LinkedHashSet<>();   // 댓글이 가질 자식 댓글


    private ArticleComment(UserAccount userAccount, Article article, String content, ArticleComment parent, LinkedHashSet<ArticleComment> children) {
        this.userAccount = userAccount;
        this.article = article;
        this.content = content;
        this.parent = parent;
        this.children = children;
    }

    public static ArticleComment of(UserAccount userAccount, Article article, String content) {   // 댓글 전용
        return new ArticleComment(userAccount, article, content, null, null);
    }

    public static ArticleComment of(UserAccount userAccount, Article article, String content, ArticleComment parent) {    // 답글 전용
        return new ArticleComment(userAccount, article, content, parent, null);
    }
}
