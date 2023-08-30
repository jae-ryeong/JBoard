package com.example.JBoard.Dto;

import com.example.JBoard.Entity.User;

public record ArticleDto(
        Long ArticleId,
        String title,
        User username,  // 이 부분은 확실하지않다 고쳐야 할거같다.
        Integer view_count
) {
}
