package com.example.JBoard.controller;

import com.example.JBoard.Dto.*;
import com.example.JBoard.Dto.Response.ArticleResponse;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final ArticleService articleService;
    private final UserService userService;
    private final ArticleCommentService commentService;
    private final PaginationService paginationService;
    private final FileService fileService;

    @GetMapping("/")
    public String index(Model model) {
        return "forward:/boardlist";
    }

    @GetMapping("/boardlist")
    public String boardlist(Model model,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "searchType", required = false) String searchType,
                            @PageableDefault(page = 0, size = 10, sort = "articleId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ArticleDtoC> articles = articleService.getPage(keyword, searchType, pageable);

        int number = articles.getNumber();
        int totalPages = articles.getTotalPages();
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(articles.getPageable().getPageNumber(), totalPages);

        model.addAttribute("Articles", articles);
        model.addAttribute("number", number);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);

        return "articles/boardList";
    }

    @GetMapping("/boardCreateForm")
    public String boardCreateForm(Model model) {
        // 스프링 시큐리티에서 필터로 걸러주기 때문에 따로 처리 X
        return "articles/boardCreateForm";
    }
    @PostMapping("/boardCreateForm")
    public String CreateForm(ArticleDtoC articleDtoC, @AuthenticationPrincipal BoardPrincipal boardPrincipal, @RequestParam("file")MultipartFile files) {
        try{
            String origFilename = files.getOriginalFilename();

            String uuid = UUID.randomUUID().toString();
            String extension = origFilename.substring(origFilename.lastIndexOf("."));
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

            //String filePath = savePath + "\\" + filename;
            String filePath = savePath + "\\" + savedName;
            System.out.println("filePath = " + filePath);
            files.transferTo(new File(filePath));

            FileDto fileDto = new FileDto(null, origFilename, savedName, filePath);

            Long fileId = fileService.saveFiles(fileDto);

            UserAccount user = userService.getUser(boardPrincipal.getUsername());
            articleService.createArticle(articleDtoC, user, fileId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/boardlist";   // @GetMapping("/boardlist") 여기로 이동
    }

    @GetMapping("/detail/{articleId}")
    public String article_detail(@PathVariable("articleId") Long articleId, Model model, @AuthenticationPrincipal BoardPrincipal boardPrincipal, HttpServletRequest request, HttpServletResponse response) {
        articleService.readArticle(articleId, request, response);
        ArticleDtoC article = articleService.getArticle(articleId);
        model.addAttribute("article", ArticleResponse.from(article));

        List<ArticleCommentDtoC> articleComments = commentService.getArticleComments(articleId);
        String fileName = fileService.getFile(article.getFileId()).orgNm();

        model.addAttribute("comments", articleComments);
        model.addAttribute("boardPrincipal", boardPrincipal);
        model.addAttribute("filename", fileName);
        return "articles/detail";
    }

    @PostMapping("/detail/{articleId}/delete")
    public String deleteArticle(@PathVariable("articleId") Long articleId, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        UserAccountDto userAccountDto = boardPrincipal.toDto();

        articleService.deleteArticle(articleId, userAccountDto);
        return "redirect:/boardlist";
    }

    @GetMapping("/update/{articleId}")
    public String updateArticleForm(@PathVariable("articleId") Long articleId, Model model, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        ArticleDtoC article = articleService.getArticle(articleId);

        if (!article.getUserAccountDto().uid().equals(boardPrincipal.uid())) {  // URI 직접 입력 방지
            return "redirect:/boardlist";
        }
        model.addAttribute("article", article);
        return "articles/boardUpdateForm";
    }

    @PostMapping("/update/{articleId}")
    public String updateArticle(@PathVariable("articleId") Long articleId, ArticleDtoC dto, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        UserAccountDto userAccountDto = boardPrincipal.toDto();
        articleService.updateArticle(articleId, dto, userAccountDto);

        System.out.println("수정완료");
        return "redirect:/detail/" + articleId;
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.savedPath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.orgNm() + "\"")
                .body(resource);
    }
}
