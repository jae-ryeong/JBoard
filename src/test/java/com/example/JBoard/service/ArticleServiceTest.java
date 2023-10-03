package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.ArticleRepository;
import com.example.JBoard.Repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class) // Mockito를 사용할 수 있게 해준다.
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;    // Repository를 @Mock으로 선언하면 Repository Bean에 의존하지 않고 테스트가 가능
    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private ArticleService articleService;

    @DisplayName("전체 게시글 조회")
    @Test
    void getArticlesTest() {
        //given
        List<Article> articles = new ArrayList<>(); // 엔티티에 직접 접근해 보았다
        UserAccountDto userAccountDto = createUserAccountDto();
        Article article = Article.of(userAccountDto.toEntity(), "제목", "내용", 0L);
        Article article2 = Article.of(userAccountDto.toEntity(), "제목", "내용", 1L);
        articles.add(article);
        articles.add(article2);

        given(articleRepository.findAll()).willReturn(articles);

        //when
        List<Article> result = articleService.getArticles();

        //then
        System.out.println("result = " + result);
        System.out.println("articles = " + articles);

        assertThat(result).isEqualTo(articles);
        assertThat(result.size()).isEqualTo(2);
    }

    @DisplayName("게시글 생성 테스트")
    @Test
    void createArticleTest() {
        //given
        ArticleDtoC article = createArticleDto();
        UserAccountDto userAccountDto = createUserAccountDto();

        given(articleRepository.save(any(Article.class))).willReturn(article.toEntity(userAccountDto.toEntity()));

        //when
        articleService.createArticle(article, userAccountDto.toEntity());

        //then
        then(articleRepository).should().save(any(Article.class));  // when 구문 없으면 테스트 실패
        assertThat(article.toEntity(userAccountDto.toEntity()).getTitle()).isEqualTo("제목");
    }

    @DisplayName("게시글 단건 조회 테스트")
    @Test
    void getArticleTest() {
        //given
        Long articleId = 1L;
        Article article = createArticle();

        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        //when
        Optional<Article> result = articleService.getArticle(articleId);

        //then
        then(articleRepository).should().findById(articleId);
        assertThat(result.get()).isEqualTo(article);
        System.out.println("result = " + result);
    }

    @DisplayName("게시글 삭제 테스트")
    @Test
    public void deleteArticleTest() throws Exception{
        //given
        Article article = createArticle();
        UserAccountDto userAccountDto = createUserAccountDto();
        UserAccount userAccount = createUserAccount();

        given(articleRepository.getReferenceById(1L)).willReturn(article);
        given(userAccountRepository.findByUid(userAccountDto.uid())).willReturn(Optional.of(userAccount));

        //when
        articleService.deleteArticle(1L, userAccountDto);

        //then
        then(articleRepository).should().deleteById(1L);

    }

    @DisplayName("게시글 수정 테스트")
    @Test
    public void updateArticleTest() throws Exception{
        //given
        Article article = createArticle();
        UserAccountDto userAccountDto = createUserAccountDto();
        UserAccount userAccount = createUserAccount();

        given(articleRepository.getReferenceById(1L)).willReturn(article);
        given(userAccountRepository.findByUid(userAccountDto.uid())).willReturn(Optional.of(userAccount));

        //when
        articleService.updateArticle(1L, createArticleDto(), userAccountDto);

        //then
        assertThat(article.getContent()).isEqualTo("내용");
    }

    private ArticleDtoC createArticleDto() {
        return ArticleDtoC.of(createUserAccountDto(),"제목", "내용", LocalDateTime.now());
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "wofud", "password", "김재령", "wofud0321@naver.com", "wofud"
        );
    }

    private UserAccount createUserAccount() {
        return UserAccount.of("wofud", "1234", "김재령", "wofud0321@naver.com", "재령");
    }

    private Article createArticle() {
        return Article.of(createUserAccountDto().toEntity(), "제목", "content", 0L);
    }
}