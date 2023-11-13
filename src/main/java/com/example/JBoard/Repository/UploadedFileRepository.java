package com.example.JBoard.Repository;

import com.example.JBoard.Entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {

    List<UploadedFile> findAllByArticleId(Long articleId);
}
