package com.example.JBoard.controller;

import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Dto.Request.ArticleCommentRequest;
import com.example.JBoard.Dto.Request.ReplyRequest;
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
    public String newComment(ArticleCommentRequest articleCommentRequest, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        commentService.saveArticleComment(articleCommentRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/detail/" + articleCommentRequest.articleId();
    }

    @PostMapping("/newReply")
    public String newReply(ReplyRequest replyRequest, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        commentService.saveReply(replyRequest.toDto(boardPrincipal.toDto()));
        return "redirect:/detail/" + replyRequest.articleId();
    }

    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        Long articleId = commentService.getArticleId(commentId);
        commentService.deleteArticleComment(commentId, boardPrincipal.toDto());
        return "redirect:/detail/" + articleId;
    }

    @PostMapping("/update/{commentId}")
    public String updateComment(@PathVariable("commentId") Long commentId, ArticleCommentRequest articleCommentRequest, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        Long articleId = commentService.getArticleId(commentId);
        commentService.updateArticleComment(commentId, articleCommentRequest, boardPrincipal.toDto());

        return "redirect:/detail/" + articleId;
    }
}
