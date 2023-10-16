package com.example.JBoard.Repository;

import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    List<ArticleComment> findAllByArticleArticleId(Long articleId);

    UserAccount findByUserAccountId(Long userId);
}
