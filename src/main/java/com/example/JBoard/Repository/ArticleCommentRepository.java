package com.example.JBoard.Repository;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    List<ArticleComment> findAllByArticleArticleId(Long articleId);

    List<ArticleComment> findAllByArticleArticleIdAndParentIsNull(Long articleId);

    UserAccount findByUserAccountId(Long userId);

    List<ArticleComment> findByArticleOrderByParentOrderAsc(Article article);

    List<ArticleComment> findByParentOrderIs
}
