package com.example.JBoard.Dto;

public record ArticleWithCommentDto(
        String content,
        String title,
        String view_count,
        String nickname
) {
}
