package com.example.JBoard.Repository;

import com.example.JBoard.Entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    List<ArticleComment> findAllByArticle_ArticleId(Long articleId);

    List<ArticleComment> findAllByArticleArticleId(Long articleId);
}
