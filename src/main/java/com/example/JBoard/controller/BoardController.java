package com.example.JBoard.controller;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final ArticleService articleService;

    @GetMapping("/boardlist")
    public String boardlist(Model model) {
        List<Article> articles = articleService.getArticles();
        model.addAttribute("Articles", articles);

        return "articles/boardList";
    }

    @GetMapping("/boardCreateForm")
    public String boardCreateForm() {
        return "articles/boardCreateForm";
    }
}
