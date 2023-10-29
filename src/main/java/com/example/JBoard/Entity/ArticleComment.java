package com.example.JBoard.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class ArticleComment extends AuditingFields{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @Column(length = 300, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id", referencedColumnName = "commentId")
    private ArticleComment parent;  // 대댓글이 가질 부모 댓글

    @OneToMany(mappedBy = "parent")
    @OrderBy("commentId asc")
    private Set<ArticleComment> children = new LinkedHashSet<>();   // 댓글이 가질 속성


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

    // TODO: jpa로 리팩토링 해보기
    public void update(String content) {
        this.content = content.replace("\r\n","<br>");
    }

    public String parentName() {
        return this.parent == null ? "" : this.parent.getUserAccount().getNickname();
    }

    public Long parentSeq() {
        return this.parent == null ? -1 : this.parent.getCommentId();
    }
}
