package com.example.JBoard.controller;

import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Dto.Request.ArticleCommentRequest;
import com.example.JBoard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final ArticleCommentService commentService;

    @PostMapping("/new")
    public String newComment(ArticleCommentRequest articleCommentRequest, @AuthenticationPrincipal BoardPrincipal boardPrincipal) { // TODO: select 후 update 쿼리가 날라간다 수정해야함
        System.out.println("articleCommentRequest = " + articleCommentRequest);
        System.out.println("댓글 생성");

        commentService.saveArticleComment(articleCommentRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/detail/" + articleCommentRequest.articleId();
    }

    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId) {
        Long articleId = commentService.getArticleId(commentId);
        commentService.deleteArticleComment(commentId);
        return "redirect:/detail/" + articleId;
    }
}
