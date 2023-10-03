package com.example.JBoard.controller;

import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.ArticleRequest;
import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Dto.UserAccountDto;
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
    public String CreateForm(ArticleDtoC articleDtoC, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication);
        UserAccount user = userService.getUser(authentication.getName());*/

        UserAccount user = userService.getUser(boardPrincipal.getUsername());
        System.out.println("컨트롤러에서 확인");
        System.out.println(articleDtoC.toString());

        articleService.createArticle(articleDtoC, user);

        return "redirect:/boardlist";   // @GetMapping("/boardlist") 여기로 이동
    }

    @GetMapping("/detail/{articleId}")
    public String article_detail(@PathVariable("articleId") Long articleId, Model model, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        model.addAttribute("article", articleService.getArticle(articleId));    // TODO: Article을 직접 반환해주는데 이를 responseDTO 생성하기
        model.addAttribute("boardPrincipal",boardPrincipal);
        return "articles/detail";
    }

    @PostMapping("/detail/{articleId}/delete")
    public String deleteArticle(@PathVariable("articleId") Long articleId,  @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        articleService.deleteArticle(articleId); // TODO: 서비스단에 인증기능 붙이기
        return "redirect:/boardlist";
    }

    @GetMapping("/update/{articleId}")
    public String updateArticleForm(@PathVariable("articleId") Long articleId, Model model) {
        Optional<Article> article = articleService.getArticle(articleId);

        model.addAttribute("article", article);
        return "articles/boardUpdateForm";
    }

    @PostMapping("/update/{articleId}")
    public String updateArticle(@PathVariable("articleId") Long articleId, ArticleDtoC dto, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        UserAccountDto userAccountDto = boardPrincipal.toDto();
        articleService.updateArticle(articleId, dto, userAccountDto);

        System.out.println("수정완료");
        return "redirect:/detail/" + articleId;
    }

    @GetMapping("/check")
    public String check() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("확인1. authentication = " + authentication);

        User user = (User)authentication.getPrincipal();
        System.out.println("확인2. user = " + user);

        return "articles/boardList";
    }
}
