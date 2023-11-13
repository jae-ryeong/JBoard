package com.example.JBoard.Dto;

import com.example.JBoard.Entity.UploadedFile;

public record FileDto(
        Long id,
        String orgNm,
        String savedNm,
        String savedPath,
        Long articleId
) {
    public static FileDto of(Long id, String orgNm, String savedNm, String savedPath, Long articleId) {
        return new FileDto(id, orgNm, savedNm, savedPath, articleId);
    }

    public UploadedFile toEntity() {
        return UploadedFile.builder()
                .id(id)
                .orgNm(orgNm)
                .savedNm(savedNm)
                .savedPath(savedPath)
                .articleId(articleId)
                .build();
    }
}
