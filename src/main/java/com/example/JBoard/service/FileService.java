package com.example.JBoard.service;

import com.example.JBoard.Dto.FileDto;
import com.example.JBoard.Entity.UploadedFile;
import com.example.JBoard.Repository.UploadedFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final UploadedFileRepository fileRepository;

    public void saveFiles(List<MultipartFile> files, Long articleId) throws IOException{

        for(MultipartFile file : files){
            // getOriginalFilename 메서드의 문제점 해결, https://developer-talk.tistory.com/811
            String origFilename = Normalizer.normalize(file.getOriginalFilename(), Normalizer.Form.NFC);

            if(origFilename != null && !origFilename.isEmpty()){
                // 파일 이름으로 쓸 uuid 생성
                String uuid = UUID.randomUUID().toString();

                // 확장자 추출(ex : .png)
                String extension = origFilename.substring(origFilename.lastIndexOf("."));

                // uuid와 확장자 결합
                String savedName = uuid + extension;

                // 실행되는 위치의 'files' 폴더에 파일이 저장
                String savePath = System.getProperty("user.dir") + "\\files";

                //파일이 저장되는 폴더가 없으면 폴더를 생성
                if (!new File(savePath).exists()) {
                    try {
                        new File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }

                String filePath = savePath + "\\" + savedName;
                file.transferTo(new File(filePath));

                FileDto fileDto = new FileDto(null, origFilename, savedName, filePath, articleId);

                fileRepository.save(fileDto.toEntity());
            }
        }
    }

    public FileDto getFile(Long id) {   // 다운로드 할때 사용
        UploadedFile file = fileRepository.getReferenceById(id);
        System.out.println("fileOrgNm = " + file.getOrgNm());

        return FileDto.of(id, file.getOrgNm(), file.getSavedNm(), file.getSavedPath(), file.getArticleId());
    }

    public List<FileDto> getFiles(Long articleId) { // detail view에 넘겨줄때 사용
        return fileRepository.findAllByArticleId(articleId).stream().map(UploadedFile::toDto).toList();
    }

    public void deleteFiles(Long articleId){    // 게시글이 삭제될 때, 연관된 파일도 삭제
        fileRepository.deleteAllByArticleId(articleId);
    }

    public List<FileDto> getFiles() { // detail view에 넘겨줄때 사용
        return fileRepository.findAll().stream().map(UploadedFile::toDto).toList();
    }

}
