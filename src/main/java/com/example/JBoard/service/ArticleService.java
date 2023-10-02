package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.ArticleRepository;
import com.example.JBoard.Repository.UserAccountRepository;
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
    private final UserAccountRepository userAccountRepository;
    
    // 반환값을 나중에 DTO로 바꿔주기

    @Transactional(readOnly = true)
    public List<Article> getArticles() {    // 모든 게시물들을 끌어온다.
        return articleRepository.findAll();
    }

    public void createArticle(ArticleDtoC articleDtoC, UserAccount userAccount) {
        System.out.println("createArticle 확인: userAccount = " + userAccount);
        Article save = articleRepository.save(articleDtoC.toEntity(userAccount));
    }

    public Optional<Article> getArticle(Long articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        article.get().addView();
        return article;
    }

    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    public void updateArticle(Long articleId, ArticleDtoC dto) {
        Article article = articleRepository.getReferenceById(articleId);

        article.update(dto.getTitle(), dto.getContent());
    }
}
