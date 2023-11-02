package com.example.JBoard.Repository;

import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.constant.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)    // In Memoery 안쓰고 기본 설정된 DB를 사용하겠다.
class ArticleRepositoryTest {   // 레포지토리 테스트는 서비스와 연결X

    private final ArticleRepository articleRepository;

    ArticleRepositoryTest(@Autowired ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Test
    public void selectTest() throws Exception{
        //when
        List<Article> articles = articleRepository.findAll();

        //then
        assertThat(articles).isNotNull().hasSize(0);
    }

    @Test
    public void saveTest() throws Exception{
        //given
        Article article = createArticle();   // view_count는 자동으로 넣어주었으면 하지만 일단 수동으로 구현

        //when
        Article save = articleRepository.save(article);
        List<Article> articles = articleRepository.findAll();

        //then
        assertThat(articles).isNotNull().hasSize(1);   // test 데이터 37 + 추가한 데이터 1
        assertThat(save.getArticleId()).isEqualTo(1);
    }

    @Test
    public void deleteTest() throws Exception{
        //given
        Article article = createArticle();

        //when
        Article save = articleRepository.save(article);
        articleRepository.deleteById(1L);

        //then
        assertThat(articleRepository.findById(1L)).isEmpty();
    }

    @Test
    public void updateTest() throws Exception{
        //given
        Article article = createArticle();

        //when
        Optional<Article> saved = articleRepository.findById(article.getArticleId());
        //articleRepository.updateArticleByTitleAndContent();

        //then
        assertThat(saved.get().getTitle()).isEqualTo("제목");
        assertThat(saved.get().getContent()).isEqualTo("내용");
    }

    Article createArticle() {
        return Article.of(createUserAccountDto().toEntity(), "title", "content", 0L);
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "wofud", "password", "김재령", "wofud0321@naver.com", "wofud", String.valueOf(MemberRole.USER)
        );
    }
}