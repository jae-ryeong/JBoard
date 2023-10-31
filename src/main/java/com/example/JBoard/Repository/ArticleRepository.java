package com.example.JBoard.Repository;

import com.example.JBoard.Entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Modifying
    @Query("update Article a set a.view_count = a.view_count+1 where a.articleId = :articleId")
    int addCount(Long articleId);

    Page<Article> findAll(Pageable pageable);

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.title LIKE %:keyword% OR a.content LIKE %:keyword% OR a.userAccount.nickname LIKE %:keyword% ")
    Page<Article> findByContentOrTitleOrNicknameContaining(String keyword, Pageable pageable);

    @Modifying
    @Query("update Article a set a.title = :title, a.content = :content where a.articleId = :articleId")
    void updateArticleByTitleAndContent(Long articleId, String title, String content);
}
