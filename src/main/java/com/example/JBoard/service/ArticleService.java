package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    
    // 반환값을 나중에 DTO로 바꿔주기

    @Transactional(readOnly = true)
    public List<Article> getArticles() {    // 모든 게시물들을 끌어온다.
        return articleRepository.findAll();
    }

    public void createArticle(ArticleDtoC articleDtoC) {
        Article save = articleRepository.save(articleDtoC.toEntity());
        System.out.println("createArticle에서 확인");
        System.out.println(save.toString());
    }

    public Optional<Article> getArticle(Long articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        return article;
    }

    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }

  /*  public void updateArticle(Long articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        articleRepository.
    }*/


}
