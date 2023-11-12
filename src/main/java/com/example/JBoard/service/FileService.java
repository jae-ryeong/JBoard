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
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final UploadedFileRepository fileRepository;
    private String fileDir = "D:/test/";

    /*public Long saveFile(MultipartFile files) throws IOException {
        if (files.isEmpty()) {
            return null;
        }

        // 파일의 오리지널 이름 추출
        String origName = files.getOriginalFilename();

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));

        // uuid와 확장자 결합
        String savedName = uuid + extension;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + savedName;

        // 파일 엔티티 생성
        UploadedFile file = UploadedFile.builder()
                .orgNm(origName)
                .savedNm(savedName)
                .savedPath(savedPath)
                .build();

        // 실제로 로컬에 uuid를 파일명으로 저장
        files.transferTo(new File(savedPath));

        // 데이터베이스에 파일 정보 저장
        UploadedFile savedFile = fileRepository.save(file);

        return savedFile.getId();
    }*/

    public Long saveFiles(MultipartFile files) throws IOException {
        /*for(file : files){

        }*/
        // getOriginalFilename 메서드의 문제점 해결, https://developer-talk.tistory.com/811
        String origFilename = Normalizer.normalize(files.getOriginalFilename(), Normalizer.Form.NFC);

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
        System.out.println("filePath = " + filePath);
        files.transferTo(new File(filePath));

        FileDto fileDto = new FileDto(null, origFilename, savedName, filePath);

        return fileRepository.save(fileDto.toEntity()).getId();
    }

    public FileDto getFile(Long id) {
        UploadedFile file = fileRepository.getReferenceById(id);
        System.out.println("fileOrgNm = " + file.getOrgNm());

        return FileDto.of(id, file.getOrgNm(), file.getSavedNm(), file.getSavedPath());
    }

}
