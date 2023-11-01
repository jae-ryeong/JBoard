package com.example.JBoard.controller;

import com.example.JBoard.Dto.ArticleCommentDtoC;
import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Dto.Response.ArticleResponse;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.service.ArticleCommentService;
import com.example.JBoard.service.ArticleService;
import com.example.JBoard.service.PaginationService;
import com.example.JBoard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final ArticleService articleService;
    private final UserService userService;
    private final ArticleCommentService commentService;
    private final PaginationService paginationService;

    @GetMapping("/")
    public String index(Model model) {
        return "forward:/boardlist";
    }

    @GetMapping("/boardlist")
    public String boardlist(Model model,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "searchType", required = false) String searchType,
                            @PageableDefault(page = 0, size = 10, sort = "articleId", direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<ArticleDtoC> articles = articleService.getPage(keyword, searchType, pageable);

        int number = articles.getNumber();
        int totalPages = articles.getTotalPages();
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(articles.getPageable().getPageNumber(), totalPages);

        model.addAttribute("Articles", articles);
        model.addAttribute("number", number);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);

        return "articles/boardList";
    }

    @GetMapping("/boardCreateForm")
    public String boardCreateForm(Model model) {
        // 스프링 시큐리티에서 필터로 걸러주기 때문에 따로 처리 X
        return "articles/boardCreateForm";
    }

    @PostMapping("/boardCreateForm")
    public String CreateForm(ArticleDtoC articleDtoC, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        UserAccount user = userService.getUser(boardPrincipal.getUsername());
        System.out.println("컨트롤러에서 확인");
        System.out.println(articleDtoC.toString());

        articleService.createArticle(articleDtoC, user);

        return "redirect:/boardlist";   // @GetMapping("/boardlist") 여기로 이동
    }

    @GetMapping("/detail/{articleId}")
    public String article_detail(@PathVariable("articleId") Long articleId, Model model, @AuthenticationPrincipal BoardPrincipal boardPrincipal, HttpServletRequest request, HttpServletResponse response) {
        articleService.readArticle(articleId, request, response);
        model.addAttribute("article", ArticleResponse.from(articleService.getArticle(articleId)));

        List<ArticleCommentDtoC> articleComments = commentService.getArticleComments(articleId);

        model.addAttribute("comments", articleComments);
        model.addAttribute("boardPrincipal",boardPrincipal);
        return "articles/detail";
    }

    @PostMapping("/detail/{articleId}/delete")
    public String deleteArticle(@PathVariable("articleId") Long articleId,  @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        UserAccountDto userAccountDto = boardPrincipal.toDto();

        articleService.deleteArticle(articleId, userAccountDto);
        return "redirect:/boardlist";
    }

    @GetMapping("/update/{articleId}")
    public String updateArticleForm(@PathVariable("articleId") Long articleId, Model model, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        ArticleDtoC article = articleService.getArticle(articleId);

        if (!article.getUserAccountDto().uid().equals(boardPrincipal.uid())) {  // URI 직접 입력 방지
            return "redirect:/boardlist";
        }
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
}
