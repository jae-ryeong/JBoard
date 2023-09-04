package com.example.JBoard.controller;

import com.example.JBoard.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleService articleService;

    BoardControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("boardlist 테스트")
    @Test
    public void boardListTest() throws Exception{
        //given
        given(articleService.getArticles()).willReturn(List.of());

        //then
        mvc.perform(get("/boardlist"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/boardList"))
                .andExpect(model().attributeExists("Articles"));
    }

    @DisplayName("[POST] boardCreateForm 테스트")
    @Test
    public void CreateFormTest() throws Exception{
        //given


        //when


        //then
    }
}