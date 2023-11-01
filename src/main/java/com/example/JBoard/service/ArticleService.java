package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.ArticleRepository;
import com.example.JBoard.Repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    //TODO: 반환값을 나중에 DTO로 바꿔주기

    @Transactional(readOnly = true)
    public List<Article> getArticles() {    // 모든 게시물들을 끌어온다.
        return articleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<ArticleDtoC> getPage(String keyword, String searchType, Pageable pageable) {    // page는 조회할 페이지 번호

        if (keyword == null && searchType == null) {
            return articleRepository.findAll(pageable).map(ArticleDtoC::from);
        } else {
            switch (searchType) {
                case "all":
                    return articleRepository.findByContentOrTitleOrNicknameContaining(keyword, pageable).map(ArticleDtoC::from);
                case "title":
                    return articleRepository.findByTitleContaining(keyword, pageable).map(ArticleDtoC::from);
                case "content":
                    return articleRepository.findByContentContaining(keyword, pageable).map(ArticleDtoC::from);
                case "nickname":
                    return articleRepository.findByUserAccount_NicknameContaining(keyword, pageable).map(ArticleDtoC::from);
            }
            ;
        }
        return articleRepository.findAll(pageable).map(ArticleDtoC::from);
    }

    public void createArticle(ArticleDtoC articleDtoC, UserAccount userAccount) {
        try {
            articleRepository.save(articleDtoC.toEntity(userAccount));
        } catch (UsernameNotFoundException e) { // 계정 없음
            log.warn("게시글 생성을 위한 계정이 로그인 되어 있지 않습니다.");
        }

    }

    /*
    쿠키를 이용해서 조회수 중복 방지
    문제점:
    1번 글을 읽는다. -> 쿠키 상태 : [1] (age 하루인 상태)
    하루가 지나기전에 다른 글을 읽는다. -> [1]_[2] (다시 age가 하루로 바뀜)
     */
    public void readArticle(Long articleId, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("cookie.getName: " + cookie.getName());
                log.info("cookie.getValue: " + cookie.getValue());

                if (cookie.getName().equals("articleView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + articleId.toString() + "]")) {
                addCount(articleId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + articleId + "]");
                oldCookie.setPath("/"); // 웹어플리케이션의 모든 URL 범위로 전송
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            addCount(articleId);
            Cookie newCookie = new Cookie("articleView", "[" + articleId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
            System.out.println("newCookie = " + newCookie);
        }

    }

    @Transactional
    public int addCount(Long articleId) {
        return articleRepository.addCount(articleId);
    }

    public ArticleDtoC getArticle(Long articleId) {
        Optional<Article> article = articleRepository.findById(articleId);
        return article.map(ArticleDtoC::from).
                orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
    }

    public void deleteArticle(Long articleId, UserAccountDto userAccountDto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            Optional<UserAccount> userAccount = userAccountRepository.findByUid(userAccountDto.uid());

            if (article.getUserAccount().getUid().equals(userAccount.get().getUid())) {
                articleRepository.deleteById(articleId);
            } else {
                log.warn("다른 사용자가 게시글 삭제를 시도했습니다.");
            }

        } catch (EntityNotFoundException e) {
            log.warn("게시글 삭제 실패. 게시글을 삭제하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }

    }

    public void updateArticle(Long articleId, ArticleDtoC dto, UserAccountDto userAccountDto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            Optional<UserAccount> userAccount = userAccountRepository.findByUid(userAccountDto.uid());

            if (article.getUserAccount().getUid().equals(userAccount.get().getUid())) {
                articleRepository.updateArticleByTitleAndContent(articleId, dto.getTitle(), dto.getContent());
            } else {
                log.warn("다른 사용자가 게시글 수정을 시도했습니다.");
            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }
}
