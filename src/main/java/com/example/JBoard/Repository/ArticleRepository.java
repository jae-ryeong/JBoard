package com.example.JBoard.Repository;

import com.example.JBoard.Entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ArticleRepository extends JpaRepository<Article, Long> {
}
