package com.example.JBoard.Entity;

import com.example.JBoard.Dto.FileDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UploadedFile { // File에 대한 정보만 저장

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID", nullable = false)
    private Long id;

    @Column(name = "ORIGINAL_NAME", nullable = false)
    private String orgNm;

    @Column(name = "SAVED_NAME", nullable = false)
    private String savedNm;

    @Column(name = "SAVED_PATH", nullable = false)
    private String savedPath;

    private Long articleId;

    @Builder
    public UploadedFile(Long id, String orgNm, String savedNm, String savedPath, Long articleId) {
        this.id = id;
        this.orgNm = orgNm;
        this.savedNm = savedNm;
        this.savedPath = savedPath;
        this.articleId = articleId;
    }

    public FileDto toDto() {
        return FileDto.of(id, orgNm, savedNm, savedPath, articleId);
    }


}
