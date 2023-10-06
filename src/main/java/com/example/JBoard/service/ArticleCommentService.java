package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleCommentDtoC;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.ArticleCommentRepository;
import com.example.JBoard.Repository.ArticleRepository;
import com.example.JBoard.Repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    public void saveArticleComment(ArticleCommentDtoC dto) {
        Article article = articleRepository.getReferenceById(dto.getArticleId());
        Optional<UserAccount> userAccount = userAccountRepository.findByUid(dto.getUserAccountDto().uid());
        articleCommentRepository.save(dto.toEntity(article, userAccount.get()));
    }

    public List<ArticleComment> getArticleComments(Long articleId) {
        return articleCommentRepository.findAllByArticleArticleId(articleId);
    }
}
