package com.example.JBoard.controller;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.ArticleRequest;
import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.service.ArticleService;
import com.example.JBoard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;
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
        return "articles/boardCreateForm";
    }

    @PostMapping("/boardCreateForm")
    public String CreateForm(ArticleDtoC articleDtoC) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount user = userService.getUser(authentication.getName());
        System.out.println("user = " + user.getUid());

        System.out.println("컨트롤러에서 확인");
        System.out.println(articleDtoC.toString());

        //System.out.println("boardPrincipal = " + boardPrincipal.getUid());

        articleService.createArticle(articleDtoC, user);
        //articleService.createArticle(articleRequest.toDto(boardPrincipal.toDto()));
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

    @GetMapping("/check")
    public String check() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("확인1. authentication = " + authentication);

        User user = (User)authentication.getPrincipal();
        System.out.println("확인2. user = " + user);

        return "articles/boardList";
    }
}
