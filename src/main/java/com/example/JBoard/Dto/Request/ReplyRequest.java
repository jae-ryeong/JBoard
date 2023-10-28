package com.example.JBoard.Dto.Request;

import com.example.JBoard.Dto.ArticleCommentDtoC;
import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Dto.ReplyDto;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.ArticleComment;

public record ReplyRequest(
        Long articleId,
        Long commentId,
        String content
) {
    public static ReplyRequest of(Long articleId,Long commentId, String content) {
        return new ReplyRequest(articleId, commentId, content);
    }

    public ReplyDto toDto(UserAccountDto userAccountDto) {
        return ReplyDto.of(articleId, commentId, content, userAccountDto);
    }
}
