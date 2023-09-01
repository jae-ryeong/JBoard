package com.example.JBoard.service;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.Repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class) // Mockito를 사용할 수 있게 해준다.
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test
    void getArticles() {
    }

    @Test
    void createArticle() {
    }

    @Test
    void getArticle() {
        //given
        long articleId = 1L;

        //when
        Optional<Article> article = articleService.getArticle(articleId);

        //then
        BDDMockito.then(articleRepository).should().findById(1L).equals(article.get().getArticleId());
    }
}