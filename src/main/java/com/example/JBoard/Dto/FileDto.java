package com.example.JBoard.Dto;

import com.example.JBoard.Entity.UploadedFile;

public record FileDto(
        Long id,
        String orgNm,
        String savedNm,
        String savedPath
) {
    public static FileDto of(Long id, String orgNm, String savedNm, String savedPath) {
        return new FileDto(id, orgNm, savedNm, savedPath);
    }

    public UploadedFile toEntity() {
        return UploadedFile.builder()
                .id(id)
                .orgNm(orgNm)
                .savedNm(savedNm)
                .savedPath(savedPath)
                .build();
    }
}
