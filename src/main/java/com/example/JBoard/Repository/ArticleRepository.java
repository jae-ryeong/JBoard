package com.example.JBoard.Repository;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Modifying
    @Query("update Article a set a.view_count = a.view_count+1 where a.articleId = :articleId")
    void addCount(Long articleId);
}
