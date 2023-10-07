package com.example.JBoard.Repository;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
