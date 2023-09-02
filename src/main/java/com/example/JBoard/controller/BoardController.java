package com.example.JBoard.controller;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PostMapping("/boardCreateForm")
    public String CreateForm(ArticleDtoC articleDtoC){
        System.out.println("컨트롤러에서 확인");
        System.out.println(articleDtoC.toString());

        articleService.createArticle(articleDtoC);
        return "redirect:/boardlist";   // @GetMapping("/boardlist") 여기로 이동
    }

    @GetMapping("/detail/{articleId}")
    public String article_detail(@PathVariable("articleId") Long articleId, Model model){
        model.addAttribute("article",articleService.getArticle(articleId));
        return "articles/detail";
    }

    @GetMapping("/detail/{articleId}/delete")
    public String deleteArticle(@PathVariable("articleId") Long articleId){
        articleService.deleteArticle(articleId);
        return "redirect:/boardlist";
    }

}
