package com.example.JBoard.controller;

import com.example.JBoard.Entity.Article;
import com.example.JBoard.service.ArticleService;
import com.example.JBoard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.JBoard.Dto.ArticleDtoC;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        return "forward:/boardlist";
    }

    @GetMapping("/boardlist")
    public String boardlist(Model model) {
        List<Article> articles = articleService.getArticles();
        model.addAttribute("Articles", articles);

        return "articles/boardList";
    }

    @GetMapping("/boardCreateForm")
    public String boardCreateForm(Model model) {
        ArticleDtoC of = ArticleDtoC.of("내용", "제목");
        articleService.createArticle(of);
        model.addAttribute("article", articleService.getArticle(38L));
        return "articles/boardCreateForm";
    }

    @PostMapping("/boardCreateForm")
    public String CreateForm(ArticleDtoC articleDtoC) {
        System.out.println("컨트롤러에서 확인");
        System.out.println(articleDtoC.toString());

        articleService.createArticle(articleDtoC);
        return "redirect:/boardlist";   // @GetMapping("/boardlist") 여기로 이동
    }

    @GetMapping("/detail/{articleId}")
    public String article_detail(@PathVariable("articleId") Long articleId, Model model) {
        model.addAttribute("article", articleService.getArticle(articleId));
        return "articles/detail";
    }

    @PostMapping("/detail/{articleId}/delete")
    public String deleteArticle(@PathVariable("articleId") Long articleId) {
        articleService.deleteArticle(articleId);
        return "redirect:/boardlist";
    }

    @GetMapping("/update/{articleId}")
    public String updateArticleForm(@PathVariable("articleId") Long articleId, Model model) {
        Optional<Article> article = articleService.getArticle(articleId);
        model.addAttribute("article", article);

        return "articles/boardUpdateForm";
    }

    @PostMapping("/update/{articleId}")
    public String updateArticle(@PathVariable("articleId") Long articleId, ArticleDtoC dto) {
        articleService.updateArticle(articleId, dto);

        System.out.println("수정완료");
        return "redirect:/detail/" + articleId;
    }

/*    @GetMapping("/")
    public void Postlogin(String uid,Model model) {
        model.addAttribute("user", userService.getUser(uid));
    }*/
}
