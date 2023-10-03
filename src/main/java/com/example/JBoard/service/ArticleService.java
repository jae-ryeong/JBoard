package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.ArticleRepository;
import com.example.JBoard.Repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
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

    public void deleteArticle(Long articleId, UserAccountDto userAccountDto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            Optional<UserAccount> userAccount = userAccountRepository.findByUid(userAccountDto.uid());

            if (article.getUserAccount().getUid().equals(userAccount.get().getUid())) {
                articleRepository.deleteById(articleId);
            }else{
                log.warn("다른 사용자가 게시글 삭제를 시도했습니다.");
            }

        }catch (EntityNotFoundException e){
            log.warn("게시글 삭제 실패. 게시글을 삭제하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }

    }

    public void updateArticle(Long articleId, ArticleDtoC dto, UserAccountDto userAccountDto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            Optional<UserAccount> userAccount = userAccountRepository.findByUid(userAccountDto.uid());

            if (article.getUserAccount().getUid().equals(userAccount.get().getUid())) {
                article.update(dto.getTitle(), dto.getContent());
            }else{
                log.warn("다른 사용자가 게시글 수정을 시도했습니다.");
            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }
}
