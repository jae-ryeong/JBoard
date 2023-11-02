package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Entity.constant.MemberRole;
import com.example.JBoard.Repository.ArticleRepository;
import com.example.JBoard.Repository.UserAccountRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.stream;
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
        List<Article> articles = new ArrayList<>();
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

    @DisplayName("게시글 페이지 조회")
    @Test
    public void getPageTest() throws Exception{
        //given
        Pageable pageable = Pageable.ofSize(10);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());

        //when
        Page<ArticleDtoC> articleDtoC = articleService.getPage(null, null, pageable);

        //then
        assertThat(articleDtoC).isEmpty();
    }

    @DisplayName("search 테스트")
    @Test
    public void searchTest() throws Exception{
        //given
        String searchType = "all";
        String keyword = "검색";
        Pageable pageable = Pageable.ofSize(10);

        given(articleRepository.findByContentOrTitleOrNicknameContaining(keyword,pageable)).willReturn(Page.empty());

        //when
        Page<ArticleDtoC> articleDtoC = articleService.getPage(keyword, searchType, pageable);

        //then
        assertThat(articleDtoC).isEmpty();
        then(articleRepository).should().findByContentOrTitleOrNicknameContaining(keyword, pageable);
    }

    @DisplayName("게시글 생성 테스트")
    @Test
    void createArticleTest() {
        //given
        ArticleDtoC article = createArticleDtoC();
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
        ArticleDtoC result = articleService.getArticle(articleId);

        //then
        then(articleRepository).should().findById(articleId);
        assertThat(result)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("articleId", article.getArticleId());
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
        ArticleDtoC articleDtoC = createArticleDtoC();

        given(articleRepository.getReferenceById(articleDtoC.getArticleId())).willReturn(article);
        given(userAccountRepository.findByUid(articleDtoC.getUserAccountDto().uid())).willReturn(Optional.of(articleDtoC.getUserAccountDto().toEntity()));

        //when
        articleService.updateArticle(articleDtoC.getArticleId(), articleDtoC, articleDtoC.getUserAccountDto());

        //then
        then(articleRepository).should().getReferenceById(1L);
        assertThat(article).hasFieldOrPropertyWithValue("title", articleDtoC.getTitle());
    }

    private ArticleDtoC createArticleDtoC() {
        return ArticleDtoC.of(1L, createUserAccountDto(),"제목", "수정내용", 0L);
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "wofud", "password", "김재령", "wofud0321@naver.com", "wofud", String.valueOf(MemberRole.USER)
        );
    }

    private UserAccount createUserAccount() {
        return UserAccount.of("wofud", "1234", "김재령", "wofud0321@naver.com", "재령");
    }

    private Article createArticle() {
        return Article.of(createUserAccountDto().toEntity(), "제목", "내용", 0L);
    }
}