package com.example.JBoard.service;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public List<Article> getArticles() {
        return articleRepository.findAll();
    }
}
