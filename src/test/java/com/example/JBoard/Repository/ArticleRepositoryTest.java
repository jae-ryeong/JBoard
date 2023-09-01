package com.example.JBoard.Repository;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ArticleRepositoryTest {

    private final ArticleRepository articleRepository;

    ArticleRepositoryTest(@Autowired ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Test
    public void selectTest() throws Exception{
        //when
        List<Article> articles = articleRepository.findAll();

        //then
        assertThat(articles).isNotNull().hasSize(37);
    }

    @Test
    public void saveTest() throws Exception{
        //given
        Article article = Article.of("title", "content", 0L);   // view_count는 자동으로 넣어주었으면 하지만 일단 수동으로 구현

        //when
        Article save = articleRepository.save(article);
        List<Article> articles = articleRepository.findAll();

        //then
        assertThat(articles).isNotNull().hasSize(38);   // test 데이터 37 + 추가한 데이터 1
        assertThat(save.getArticleId()).isEqualTo(38);
    }
}