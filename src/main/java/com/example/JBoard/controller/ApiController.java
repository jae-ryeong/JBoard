package com.example.JBoard.controller;

import com.example.JBoard.Dto.ArticleCommentDto;
import com.example.JBoard.Dto.ArticleCommentDtoC;
import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.service.ArticleCommentService;
import com.example.JBoard.service.ArticleService;
import com.example.JBoard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final ArticleService articleService;
    private final ArticleCommentService commentService;

    @GetMapping("/my-page/myArticles")
    public ResponseEntity<Page<ArticleDtoC>> myArticles(@AuthenticationPrincipal BoardPrincipal boardPrincipal, @PageableDefault(page = 0, size = 10, sort = "articleId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ArticleDtoC> articleDtoCS = articleService.myArticle(boardPrincipal.uid(), pageable);
        return new ResponseEntity<>(articleDtoCS, HttpStatus.OK);
    }

    @GetMapping("/my-page/myComments")
    public ResponseEntity<List<ArticleCommentDtoC>> myComments(@AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        List<ArticleCommentDtoC> articleCommentDtoCS = commentService.myComments(boardPrincipal.uid());
        return new ResponseEntity<>(articleCommentDtoCS, HttpStatus.OK);
    }
}
