package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleCommentDtoC;
import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.ArticleCommentRepository;
import com.example.JBoard.Repository.ArticleRepository;
import com.example.JBoard.Repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @Mock
    ArticleCommentRepository commentRepository;
    @Mock
    ArticleRepository articleRepository;
    @Mock
    UserAccountRepository userAccountRepository;

    @InjectMocks
    ArticleCommentService commentService;
    @InjectMocks
    ArticleService articleService;

    @DisplayName("댓글 생성 서비스 테스트")
    @Test
    void saveArticleCommentTest() {
        //given
        Article article = createArticle();
        ArticleComment articleComment = createArticleComment();
        ArticleCommentDtoC commentDto = createCommentDto();
        UserAccount userAccount = createUserAccount();
        UserAccountDto userAccountDto = createUserAccountDto();
        ArticleDtoC articleDto = createArticleDto();

//        given(articleRepository.save(any(Article.class))).willReturn(articleDto.toEntity(userAccountDto.toEntity()));
//        given(userAccountRepository.save(any(UserAccount.class))).willReturn(userAccount);
        given(userAccountRepository.findByUid(userAccountDto.uid())).willReturn(Optional.of(userAccount));
        given(articleRepository.save(any(Article.class))).willReturn(article);
        given(commentRepository.save(any(ArticleComment.class))).willReturn(articleComment);

        //when
        articleService.createArticle(articleDto, userAccountDto.toEntity());
        commentService.saveArticleComment(commentDto);

        //then
        System.out.println(articleRepository.count());
        //assertThat(articleRepository.findAll().size()).isEqualTo(1);
        //assertThat(commentRepository.findAll().size()).isEqualTo(1);

    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "wofud", "password", "김재령", "wofud0321@naver.com", "wofud"
        );
    }
    private UserAccount createUserAccount() {
        return UserAccount.of("wofud", "1234", "김재령", "wofud0321@naver.com", "재령");
    }

    private ArticleDtoC createArticleDto() {
        return ArticleDtoC.of(createUserAccountDto(),"제목", "내용", LocalDateTime.now());
    }
    private Article createArticle() {
        return Article.of(createUserAccountDto().toEntity(), "제목", "content", 0L);
    }

    private ArticleCommentDtoC createCommentDto() {
        return ArticleCommentDtoC.of(1L, createUserAccountDto(), "댓글");
    }

    private ArticleComment createArticleComment() {
        return ArticleComment.of(createUserAccountDto().toEntity(), createArticle(), "댓글");
    }
}