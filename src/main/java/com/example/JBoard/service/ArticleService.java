package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleDtoC;
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
    public List<Article> getArticles() {    // 모든 게시물들을 끌어온다.
        return articleRepository.findAll();
    }

    public void createArticle(ArticleDtoC articleDtoC) {
        Article save = articleRepository.save(articleDtoC.toEntity());
        System.out.println("createArticle에서 확인");
        System.out.println(save.toString());
    }


}
