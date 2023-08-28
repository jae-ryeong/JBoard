package com.example.JBoard.controller;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final ArticleService articleService;

    @GetMapping("/boardlist")
    public String boardlist(Model model) {
        model.addAttribute("Articles", Article.class);
        return "articles/boardList";
    }

    @GetMapping("/boardCreateForm")
    public String boardCreateForm() {
        return "articles/boardCreateForm";
    }
}
