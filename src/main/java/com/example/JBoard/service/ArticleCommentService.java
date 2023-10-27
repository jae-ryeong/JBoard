package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleCommentDto;
import com.example.JBoard.Dto.ArticleCommentDtoC;
import com.example.JBoard.Dto.Request.ArticleCommentRequest;
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
import java.util.stream.Collectors;

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

        Long parentOrder = article.getArticleComment().stream().count();

        articleCommentRepository.save(dto.toEntity(article, userAccount.get(), parentOrder));
    }

    @Transactional(readOnly = true)
    public List<ArticleCommentDtoC> getArticleComments(Long articleId) {
        return articleCommentRepository.findAllByArticleArticleId(articleId)
                .stream().map(ArticleCommentDtoC::from)
                .toList();
    }

    public void deleteArticleComment(Long commentId) {
        articleCommentRepository.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    public Long getArticleId(Long commentId) {
        ArticleComment comment = articleCommentRepository.getReferenceById(commentId);
        Long articleId = comment.getArticle().getArticleId();
        return articleId;
    }


    public void updateArticleComment(Long commentId, ArticleCommentRequest articleCommentRequest) {
        ArticleComment comment = articleCommentRepository.getReferenceById(commentId);

        comment.update(articleCommentRequest.content());
    }
}
