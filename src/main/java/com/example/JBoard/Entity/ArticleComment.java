package com.example.JBoard.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    private Long parentOrder;   // 댓글의 그룹
    @Setter
    private Long REF_STEP;  // 댓글의 순서 TODO: 필요 없을 수도 있다. 그때 삭제하도록 하자
    @Setter
    private Long REF_LEVEL; // 댓글의 깊이, 0은 게시물에 직접적으로 달린 댓글, 1부터 대댓글

    @Setter // TODO: 구현이 완료되면 리팩토링으로 setter를 지울것이다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent", referencedColumnName = "commentId")
    private ArticleComment parent;     // 대댓글이 어떤 댓글에 답글을 단 건지 적는 필드이다. 게시물에 바로 다는 댓글은 부모댓글이 없기 때문에 -1로 적어 넣는다.


    private ArticleComment(UserAccount userAccount, Article article, String content, Long parentOrder) {
        this.userAccount = userAccount;
        this.article = article;
        this.content = content;
        this.parentOrder = parentOrder;
    }

    public static ArticleComment of(UserAccount userAccount, Article article, String content, Long parentOrder) {
        return new ArticleComment(userAccount, article, content, parentOrder);
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
