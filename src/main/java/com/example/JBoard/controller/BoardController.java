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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
    public String CreateForm(ArticleDtoC articleDtoC, @AuthenticationPrincipal BoardPrincipal boardPrincipal, @RequestParam("file") List<MultipartFile> files) {
        try {
            UserAccount user = userService.getUser(boardPrincipal.getUsername());
            Long articleId = articleService.createArticle(articleDtoC, user);
            System.out.println("files = " + files);
            fileService.saveFiles(files, articleId);
        } catch (Exception e) {
            e.getStackTrace();
        }

        return "redirect:/boardlist";   // @GetMapping("/boardlist") 여기로 이동
    }

    @GetMapping("/detail/{articleId}")
    public String article_detail(@PathVariable("articleId") Long articleId, Model model, @AuthenticationPrincipal BoardPrincipal boardPrincipal, HttpServletRequest request, HttpServletResponse response) {
        articleService.readArticle(articleId, request, response);
        ArticleDtoC article = articleService.getArticle(articleId);

        List<ArticleCommentDtoC> articleComments = commentService.getArticleComments(articleId);

        List<FileDto> files = fileService.getFiles(article.getArticleId());

        model.addAttribute("article", ArticleResponse.from(article));
        model.addAttribute("comments", articleComments);
        model.addAttribute("boardPrincipal", boardPrincipal);
        model.addAttribute("files", files);
        return "articles/detail";
    }

    @PostMapping("/detail/{articleId}/delete")
    public String deleteArticle(@PathVariable("articleId") Long articleId, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        UserAccountDto userAccountDto = boardPrincipal.toDto();

        articleService.deleteArticle(articleId, userAccountDto);
        fileService.deleteFiles(articleId);
        return "redirect:/boardlist";
    }

    @GetMapping("/update/{articleId}")
    public String updateArticleForm(@PathVariable("articleId") Long articleId, Model model, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        ArticleDtoC article = articleService.getArticle(articleId);

        if (!article.getUserAccountDto().uid().equals(boardPrincipal.uid())) {  // URI 직접 입력 방지
            return "redirect:/boardlist";
        }

        List<FileDto> files = fileService.getFiles(articleId);

        model.addAttribute("files", files);
        model.addAttribute("article", article);
        return "articles/boardUpdateForm";
    }

    @PostMapping("/update/{articleId}")
    public String updateArticle(@PathVariable("articleId") Long articleId, ArticleDtoC dto, @RequestParam(name = "file", required = false) List<MultipartFile> files, @AuthenticationPrincipal BoardPrincipal boardPrincipal) throws IOException {
        UserAccountDto userAccountDto = boardPrincipal.toDto();
        articleService.updateArticle(articleId, dto, userAccountDto);

        System.out.println("files = " + files);
        if(files != null || !files.isEmpty()) {
            fileService.deleteFiles(articleId);
            fileService.saveFiles(files, articleId);
        }
        return "redirect:/detail/" + articleId;
    }

    @GetMapping("/download/{fileId}")   
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        try{
            FileDto fileDto = fileService.getFile(fileId);
            Path path = Paths.get(fileDto.savedPath());
            Resource resource = new InputStreamResource(Files.newInputStream(path));

            return ResponseEntity.ok()
                    // "application/octet-stream"은 자바에서 사용하는 파일 다운로드 응답 형식
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    // "attachment;fileName="을 사용하여 다운로드시 파일 이름을 지정
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileDto.orgNm(),"UTF-8") + "\"")
                    .body(resource);
        } catch (NoSuchFileException e){ // TODO: 예외처리 바꿔주기
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(null);
        }
    }
}
