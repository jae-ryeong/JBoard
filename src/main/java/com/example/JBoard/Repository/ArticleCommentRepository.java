package com.example.JBoard.Repository;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    List<ArticleComment> findAllByArticleArticleIdAndParentIsNull(Long articleId);

    @Modifying
    @Query(value = "update article_comment set content = replace(?1, '\r\n', '<br>') where comment_Id = ?2", nativeQuery = true)
    void updateComment(String content, Long commentId);
}
