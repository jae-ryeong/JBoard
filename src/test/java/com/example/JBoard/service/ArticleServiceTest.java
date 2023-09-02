package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class) // Mockito를 사용할 수 있게 해준다.
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;    // Repository를 @Mock으로 선언하면 Repository Bean에 의존하지 않고 테스트가 가능

    @InjectMocks
    private ArticleService articleService;

    @DisplayName("전체 게시글 조회")
    @Test
    void getArticlesTest() {
        //given
        List<Article> articles = new ArrayList<>(); // 엔티티에 직접 접근해 보았다
        Article article = Article.of("제목", "내용", 0L);
        Article article2 = Article.of("제목", "내용", 1L);
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

        given(articleRepository.save(any(Article.class))).willReturn(article.toEntity());

        //when
        articleService.createArticle(article);

        //then
        then(articleRepository).should().save(any(Article.class));  // when 구문 없으면 테스트 실패
        assertThat(article.toEntity().getTitle()).isEqualTo("제목");
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

        //when
        articleService.deleteArticle(article.getArticleId());

        //then
        then(articleRepository).should().deleteById(article.getArticleId());

    }

    private ArticleDtoC createArticleDto() {
        return ArticleDtoC.of("제목", "내용");
    }

    private Article createArticle() {
        return Article.of("제목", "content", 0L);
    }
}