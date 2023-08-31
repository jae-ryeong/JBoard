package com.example.JBoard.Dto;

import com.example.JBoard.Entity.User;

public record ArticleDto(
        Long ArticleId,
        String title,
        Integer view_count
) {
}
